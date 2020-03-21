package ru.paswd.infosearch.wikidocumentlistcreator.api.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class ContentResponse (
    @JsonProperty("parse")
    var data: Data?
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Data (
        @JsonProperty("title")
        var title: String?,

        @JsonProperty("pageid")
        var pageId: Long?,

        @JsonProperty("text")
        var text: String?
    )
}