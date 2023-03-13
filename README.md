# Enver [![](https://jitpack.io/v/net.lsafer/enver.svg)](https://jitpack.io/#net.lsafer/enver)

Environment Variables Solution for kotlin

### Install

The main way of installing this library is
using `jitpack.io`

```kts
repositories {
    // ...
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    // Replace TAG with the desired version
    implementation("net.lsafer:enver:TAG")
}
```

### How to directly access variables

The following is an example of accessing the
current known value of an environment variable
from some enver instance.

```kotlin
fun doSomething() {
    Enver["MY_ENV_VAR"]
}
```

### How to access variables using properties

The following are examples of declaring
environment variable properties.

```kotlin
val variableOrNull: String? by Enver("MY_ENV_VAR")

val variableOrDefault: String by Enver("MY_ENV_VAR", "22")

val variableOrThrow: String by Enver.required("MY_ENV_VAR")

val transformedVariableOrNull: Int? by Enver("MY_ENV_VAR") { it?.toInt() }

val transformedVariableOrDefault: Int by Enver("MY_ENV_VAR", "22") { it.toInt() }

val transformedVariableOrThrow: Int by Enver.required("MY_ENV_VAR") { it.toInt() }
```

### How to populate the instance

Every `Enver` instance is initialized with no data
and the population process is left for the user to
manually do it.

This way, the `Enver` can be populated from multiple
different sources with different ordering.

The following is an example of using all different
kinds of built-in sources:

```kotlin
fun main() {
    // populate from System.getenv()
    Enver += enverSystem()
    // populate from parsing File(".env")
    Enver += enverFile(".env")
    // populate from parsing bundled file ".env"
    Enver += enverResource(".env")
    // populate from parsing a dotenv literal
    Enver += enverSource("MY_ENV_VAR=22\nMY_ENV_VAR_2=442")
    // populate from a Map
    Enver += mapOf("MY_ENV_VAR" to "22", "MY_ENV_VAR_2" to "442")
}
```
