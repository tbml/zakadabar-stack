# Communication

Communication functions built into the stack automatically handle:

- entity CRUD
- action and query execution
- BLOBs (with the `lib:blobs` module included)

Check the [Communication Basics](https://zakadabar.io/cookbook/Communication-Basics)
cookbook recipe to see how to use and the [URLs](URLs.md) guide for the HTTP
implementation details.

Above the basics you can configure the communication to have additional
features such as:

- local cache
- request routing  
- multiple endpoints
- configurable endpoints
- retry policy
- change notifications 

## Terminology


### comm

Each business object class - except BaseBo - have a companion object that
automatically creates a so-called `comm`. `comm` handles the communication
between nodes running a stack based application. 

`comm` is an actual object instance stored in the `comm` property of the companion 
object. This instance is shared between all the business object instances.

## Configuration

To configure the communication provide the configuration to the business object
companion:

```kotlin
@Serializable
class ExampleBo(
    override var id: EntityId<ExampleBo>
) : EntityBo<ExampleBo> {

    companion object : EntityBoCompanion<ExampleBo>("zkc-example-bo") {
        configure(comm) {
            cache = LocalCache()
        }
    }

    override fun getBoNamespace() = boNamespace
    override fun comm() = comm

}
```


