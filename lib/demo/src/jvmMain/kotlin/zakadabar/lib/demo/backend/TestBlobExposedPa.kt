/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.demo.backend

import zakadabar.lib.blobs.backend.BlobExposedPa
import zakadabar.lib.blobs.backend.BlobExposedTable
import zakadabar.lib.demo.data.TestBlob
import zakadabar.stack.backend.util.default

class TestBlobExposedPa : BlobExposedPa<TestBlob>(
    table = TestBlobExposedTable,
) {
    override fun newInstance() = default<TestBlob> {  }
}

object TestBlobExposedTable : BlobExposedTable<TestBlob>(
    tableName = "test_blob",
    referenceTable = TestExposedTableGen
)
