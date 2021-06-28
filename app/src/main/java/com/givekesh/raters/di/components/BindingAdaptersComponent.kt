package com.givekesh.raters.di.components

import com.givekesh.raters.di.scopes.BindingAdaptersScope
import dagger.hilt.DefineComponent
import dagger.hilt.components.SingletonComponent

@BindingAdaptersScope
@DefineComponent(parent = SingletonComponent::class)
interface BindingAdaptersComponent


@DefineComponent.Builder
interface BindingAdaptersComponentBuilder {
    fun build(): BindingAdaptersComponent
}