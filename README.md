# Cache Android

A simple Android utility for caching objects using the Android cache directory. This example demonstrates how to save, retrieve, and delete cached objects in a generic way using JSON serialization with GSON.

## Features

- **Object Caching**: Save and retrieve any Java object into the Android cache directory as JSON.
- **JSON Serialization**: Uses GSON to serialize objects into JSON format.
- **Cache Management**: Supports retrieving, deleting single cache entries, or clearing the entire cache.
- **Thread Safety**: Makes use of try-with-resources for safe file handling.

## Getting Started

### Installation

Add the following dependency to your `build.gradle` file:

```groovy
dependencies {
    implementation 'com.google.code.gson:gson:2.8.7'
}
```

## Usage
### Initialize ObjectCache

```java
ObjectCache<MyObject> cache = new ObjectCache<>(context);
```

### Store Data in Cache

```java
MyObject object = new MyObject();
cache.setValue("my_key", object);
```

### Retrieve Data from Cache

```java
MyObject cachedObject = cache.getValue("my_key", MyObject.class);
if (cachedObject != null) {
    // Use the cached data
} else {
    // Handle cache miss
}
```

### Delete Cached Data

```java
cache.deleteValue("my_key");
```

### Clear Entire Cache

```java
cache.deleteAllValues();
```
