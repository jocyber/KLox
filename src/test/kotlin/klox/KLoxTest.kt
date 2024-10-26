package klox

import assertk.assertThat
import assertk.assertions.*
import io.kotest.core.spec.style.StringSpec

class KLoxTest : StringSpec({
    "1 + 1 = 2" {
        assertThat(1 + 1).isEqualTo(2) 
    }
})
