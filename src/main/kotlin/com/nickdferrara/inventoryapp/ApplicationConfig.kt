package com.nickdferrara.inventoryapp

import com.nickdferrara.inventoryapp.security.RsaKeyProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(RsaKeyProperties::class)
class ApplicationConfig