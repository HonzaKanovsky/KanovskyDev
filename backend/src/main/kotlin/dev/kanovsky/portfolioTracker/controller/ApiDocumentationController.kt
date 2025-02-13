package dev.kanovsky.portfolioTracker.controller

import dev.kanovsky.portfolioTracker.annotations.ApiDescription
import dev.kanovsky.portfolioTracker.dto.ApiEndpointInfoDTO
import dev.kanovsky.portfolioTracker.dto.ApiFieldInfoDTO
import dev.kanovsky.portfolioTracker.dto.ApiParameterInfoDTO
import org.springframework.context.ApplicationContext
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
import kotlin.reflect.full.memberProperties

/**
 * Generates API documentation dynamically by scanning all controllers and extracting their endpoints.
 **/
@RestController
@RequestMapping(value = ["/api/endpoints"])
class ApiDocumentationController(private val applicationContext: ApplicationContext) {

    @GetMapping
    fun getApiEndpoints(): List<ApiEndpointInfoDTO> {
        val handlerMapping = applicationContext.getBean(RequestMappingHandlerMapping::class.java)
        val endpoints = mutableListOf<ApiEndpointInfoDTO>()

        for ((requestMappingInfo, handlerMethod) in handlerMapping.handlerMethods) {
            val paths = requestMappingInfo.pathPatternsCondition?.patterns?.map { it.patternString }
                ?: requestMappingInfo.patternsCondition?.patterns
                ?: emptySet()

            val methods = requestMappingInfo.methodsCondition.methods

            for (path in paths) {
                for (method in methods) {
                    val parameters = handlerMethod.method.parameters.map { param ->
                        val type = param.type.kotlin
                        val fields = if (type.isData) {
                            type.memberProperties.map { prop ->
                                ApiFieldInfoDTO(prop.name, prop.returnType.toString().substringAfter('.'))
                            }
                        } else {
                            null
                        }

                        ApiParameterInfoDTO(param.name ?: "unknown", type.simpleName ?: "unknown", fields)
                    }

                    val description = getEndpointDescription(handlerMethod)

                    endpoints.add(
                        ApiEndpointInfoDTO(
                            path = path,
                            method = method.name,
                            description = description,
                            parameters = parameters
                        )
                    )
                }
            }
        }

        return endpoints
    }

    private fun getEndpointDescription(handlerMethod: HandlerMethod): String {
        val method = handlerMethod.method
        val annotation = AnnotationUtils.findAnnotation(method, ApiDescription::class.java)
        return annotation?.value ?: "No description available"
    }
}
