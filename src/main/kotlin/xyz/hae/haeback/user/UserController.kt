package xyz.hae.haeback.user

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriComponentsBuilder

@RestController
class UserController {

    @Value("\${oauth2.google.client-id}")
    lateinit var clientId: String

    @Value("\${oauth2.google.scope}")
    lateinit var scope: String

    @Value("\${oauth2.google.response_type}")
    lateinit var responseType: String

    @Value("\${oauth2.google.access_type}")
    lateinit var accessType: String

    @Value("\${oauth2.google.redirect_uri}")
    lateinit var redirectUri: String

    private val logger = KotlinLogging.logger {}

    // RestTemplate 방식
    @PostMapping("/restTemplate")
    fun createUserForRestTemplate(@RequestBody accessToken: String) {
        logger.debug { "accessToken: ${accessToken}" }
        // TODO accessToken 저장 필요, 구글 찔러보기

        val restTemplate = RestTemplate()

        val response: ResponseEntity<String> =
            restTemplate.getForEntity("https://www.googleapis.com/auth/contacts", String::class.java)

        if (response.statusCode.is2xxSuccessful) {
            val responseBody: String? = response.body
            println("Response: $responseBody")
        } else {
            println("Failed to retrieve data")
        }

    }

    /**
     * webflux 가 restTemplate 을 대체하는 추세라고 해서 작성해봄
     */
    @PostMapping("/oauth2/authorization/{provider}")
    fun requestLogin(@PathVariable provider: String): String {

        println("provider: $provider")

        val webClient = WebClient.create()

        val url = UriComponentsBuilder.fromUriString("https://accounts.google.com/o/oauth2/v2/auth")
            .queryParam("scope", scope)
            .queryParam("access_type", accessType)
            .queryParam("response_type", responseType)
            .queryParam("redirect_uri", redirectUri)
            .queryParam("client_id", clientId)
            .build()
            .toUriString()

        println("url: ${url}")

        return url
//        webClient.get()
//            .uri(url)
//            .retrieve()
//            .bodyToMono(String::class.java)
//            .block()
//
//        val objectMapper = jacksonObjectMapper()
//
//        val jsonString = objectMapper.writeValueAsString(accessToken)
//
//
//        val response = webClient.get()
//            .retrieve()
//            .toEntity(String::class.java)
//            .block()
//
//        val headers = response?.headers
//
//        println("response headers: $headers")
//        println("response body: ${response?.body}")
    }

    @PostMapping("/login/oauth2/code/{provider}")
    fun getRedirect(
        @PathVariable provider: String,
        @RequestBody map: Map<String, String>
    ) {


    }
}