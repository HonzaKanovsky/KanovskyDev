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

    /**
     * Retrieves a list of all API endpoints dynamically by inspecting the request mappings.
     *
     * @return A list of `ApiEndpointInfoDTO` objects containing metadata about each API endpoint.
     */
    @GetMapping
    @ApiDescription("Return all endpoints")
    fun getApiEndpoints(): List<ApiEndpointInfoDTO> {
        // Obtain the request handler mapping bean, which stores all controller request mappings
        val handlerMapping = applicationContext.getBean(RequestMappingHandlerMapping::class.java)
        val endpoints = mutableListOf<ApiEndpointInfoDTO>()

        // Iterate over all registered request mappings
        for ((requestMappingInfo, handlerMethod) in handlerMapping.handlerMethods) {
            // Extract all paths associated with the endpoint
            val paths = requestMappingInfo.pathPatternsCondition?.patterns?.map { it.patternString }
                ?: requestMappingInfo.patternsCondition?.patterns
                ?: emptySet()

            // Extract allowed HTTP methods for the endpoint
            val methods = requestMappingInfo.methodsCondition.methods

            // Iterate over each registered path
            for (path in paths) {
                for (method in methods) {
                    // Extract method parameters, including DTO field details if applicable
                    val parameters = handlerMethod.method.parameters.map { param ->
                        val type = param.type.kotlin

                        // If the parameter is a data class, extract its fields
                        val fields = if (type.isData) {
                            type.memberProperties.map { prop ->
                                ApiFieldInfoDTO(prop.name, prop.returnType.toString().substringAfter('.'))
                            }
                        } else {
                            null
                        }

                        // Create an `ApiParameterInfoDTO` object for each parameter
                        ApiParameterInfoDTO(param.name ?: "unknown", type.simpleName ?: "unknown", fields)
                    }

                    // Retrieve the endpoint description from the `@ApiDescription` annotation (if present)
                    val description = getEndpointDescription(handlerMethod)

                    // Create and add the API endpoint metadata to the list
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

    /**
     * This method scans for the `@ApiDescription` annotation on the handler method
     * and extracts the description if present. If the annotation is missing,
     * it defaults to `"No description available"`.
     *
     * @param handlerMethod The Spring `HandlerMethod` representing the endpoint.
     * @return The description of the endpoint.
     */
    private fun getEndpointDescription(handlerMethod: HandlerMethod): String {
        val method = handlerMethod.method
        val annotation = AnnotationUtils.findAnnotation(method, ApiDescription::class.java)
        return annotation?.value ?: "No description available"
    }
}
