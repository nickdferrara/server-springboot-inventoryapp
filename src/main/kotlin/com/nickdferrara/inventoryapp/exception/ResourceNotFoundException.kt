package com.nickdferrara.inventoryapp.exception

class ResourceNotFoundException(message: String) : RuntimeException(message) {
    constructor(resourceType: String, resourceId: Any) : this("$resourceType with id $resourceId not found")
}