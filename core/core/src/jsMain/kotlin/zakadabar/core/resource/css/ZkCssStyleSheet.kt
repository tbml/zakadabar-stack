/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.resource.css

import kotlinx.browser.document
import org.w3c.dom.HTMLElement
import zakadabar.core.resource.ZkTheme
import zakadabar.core.resource.css.ZkCssStyleSheet.Companion.styleSheets
import zakadabar.core.resource.theme
import zakadabar.core.resource.themeIsInitialized
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Represents a CSS style sheet in the browser. Provides programmatic
 * tools to build, maintain and merge style sheets. Uses [theme]
 * to build the styles.
 *
 * I decided to add a type parameter because that makes themes easily extendable.
 * However, the theme in the application is not typed, so this is not a perfect
 * solution. We can go with this for now and then figure out how to resolve
 * this conflict.
 */
open class ZkCssStyleSheet {

    companion object {
        var nextId = 0

        /**
         * When true style names will be like "zks233" where the number
         * is the id assigned to the ZkCssStyleRule. When false, the class
         * names will contain the style class name, the property name and the id.
         */
        var shortNames = false

        val styleSheets = mutableListOf<ZkCssStyleSheet>()
    }

    val id = nextId ++

    /**
     * When true, the next refresh attaches this style sheet.
     * Style sheets have to wait until the application is set.
     */
    open var attachOnRefresh = false

    val element = document.createElement("style")

    init {
        element.id = "zk-${this::class.simpleName}-$id"
    }

    open val theme: ZkTheme
        get() = zakadabar.core.resource.theme

    internal val rules = mutableMapOf<String, ZkCssStyleRule>() // key is property name

    internal val parameters = mutableListOf<ZkCssParameter<*>>()

    fun cssImport(builder: ZkCssStyleRule.(ZkTheme) -> Unit) = CssDelegateProvider(null, "@import", null, builder)

    fun cssRule(selector: String, builder: ZkCssStyleRule.(ZkTheme) -> Unit) = CssDelegateProvider(null, selector, null, builder)

    fun cssClass(builder: ZkCssStyleRule.(ZkTheme) -> Unit) = CssDelegateProvider(null, null, null, builder)

    fun cssClass(on: () -> String, builder: ZkCssStyleRule.(ZkTheme) -> Unit) =
        CssDelegateProvider(null, null, on, builder)

    fun cssClass(name: String? = null, builder: ZkCssStyleRule.(ZkTheme) -> Unit) = CssDelegateProvider(name, null, null, builder)

    /**
     * Reset all the parameters to their initial value.
     */
    fun resetParameters() {
        parameters.forEach { it.reset() }
    }

    /**
     * Called before the compilation of the style sheet. Short, last-minute CSS
     * rule modifications may be put here.
     */
    open fun onConfigure() {

    }

    fun attach() {

        if (document.getElementById(element.id) != null) {
            refresh()
            return
        }

        if (this !in styleSheets) {
            styleSheets += this
        }

        var sheets = document.getElementById("zk-styles")

        if (sheets == null) {
            sheets = document.createElement("div") as HTMLElement
            sheets.id = "zk-styles"
            document.body?.appendChild(sheets)
        }

        sheets.appendChild(element)

        refresh()
    }

    fun detach() {
        element.remove()
        styleSheets -= this
    }

    protected fun refresh() {
        rules.forEach {
            it.value.build()
        }

        onConfigure()

        element.innerHTML = rules.map { it.value.compile() }.joinToString("\n")
    }

    fun onThemeChange() {
        attach()
    }
}

fun <S : ZkCssStyleSheet> cssStyleSheet(sheet: S) = CssStyleSheetDelegate(sheet)

/**
 * Handles the style sheets. Initial attach is delayed until the theme is
 * initialized. More precisely, initial attach does not happen when
 * the application is not initialized yet. In this case, the initTheme
 * method of the application will run a refresh and that refresh attaches
 * the sheet.
 */
class CssStyleSheetDelegate<S : ZkCssStyleSheet>(
    protected var sheet: S?
) {

    init {
        sheet?.let {
            styleSheets.add(it)
            if (themeIsInitialized) {
                it.attach()
            } else {
                it.attachOnRefresh = true
            }
        }
    }

    operator fun getValue(thisRef: Nothing?, property: KProperty<*>): S = get()

    operator fun getValue(thisRef: Any?, property: KProperty<*>): S = get()

    operator fun setValue(thisRef: Nothing?, property: KProperty<*>, value: S) = set(value)

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: S) = set(value)

    fun get() : S = sheet ?: throw IllegalStateException("style sheet not initialized yet")

    fun set(value: S) {
        sheet?.detach()

        sheet = value
        styleSheets.add(value)

        if (themeIsInitialized) {
            theme.onResume() // FIXME make this sheet specific
            value.attach()
        } else {
            sheet?.attachOnRefresh = true
        }
    }

}

/**
 * Provides the ZkCssStyleRule as a delegate. I decided to go this way because I think it is important
 * for the rule to know the property name and that's not possible with a simple assignment. Performance
 * overhead should be minimal.
 */
class CssDelegateProvider(
    val name: String? = null,
    val selectorString: String? = null,
    val selectorFunc: (() -> String)? = null,
    val builder: ZkCssStyleRule.(ZkTheme) -> Unit
) {

    operator fun provideDelegate(thisRef: ZkCssStyleSheet, prop: KProperty<*>): ReadOnlyProperty<ZkCssStyleSheet, ZkCssStyleRule> {

        val cssClassName = name ?: if (ZkCssStyleSheet.shortNames) "zks${ZkCssStyleSheet.nextId ++}" else "${thisRef::class.simpleName}-${prop.name}-${ZkCssStyleSheet.nextId ++}"

        // this is pretty hackish, but direct selectors are rarely used
        // TODO check the performance impact of selector manipulation

        val sf = when {
            selectorFunc != null -> selectorFunc
            selectorString != null -> fun(): String { return selectorString }
            else -> null
        }

        val rule = ZkCssStyleRule(thisRef, prop.name, cssClassName, sf, builder)

        thisRef.rules[prop.name] = rule

        return rule
    }

}

/**
 * Use this function to define style sheet parameters. When defined with this
 * function the theme switching and state saving will work seamlessly.
 */
fun <T> cssParameter(initializer: () -> T) = CssParameterProvider(initializer)

/**
 * Provides the ZkCssStyleRule as a delegate. I decided to go this way because I think it is important
 * for the rule to know the property name and that's not possible with a simple assignment. Performance
 * overhead should be minimal.
 */
class CssParameterProvider<T>(val initializer: () -> T) {

    operator fun provideDelegate(thisRef: ZkCssStyleSheet, prop: KProperty<*>) =
        ZkCssParameter(initializer).also { thisRef.parameters += it }

}

private object UNINITIALIZED

/**
 * A style sheet parameter. Purpose of this class is to have the [reset] method.
 * During theme change all style sheets are reset before onResume runs.
 */
class ZkCssParameter<T>(
    val initializer: () -> T
) : ReadWriteProperty<ZkCssStyleSheet, T> {

    protected var _value: Any? = UNINITIALIZED
    protected var _initializer: (() -> T)? = initializer

    @Suppress("UNCHECKED_CAST") // initializer will be checked
    override fun getValue(thisRef: ZkCssStyleSheet, property: KProperty<*>): T {
        if (_value === UNINITIALIZED) {
            _value = _initializer !!()
            _initializer = null
        }
        return _value as T
    }

    override fun setValue(thisRef: ZkCssStyleSheet, property: KProperty<*>, value: T) {
        this._value = value
    }

    fun reset() {
        _value = initializer()
    }
}