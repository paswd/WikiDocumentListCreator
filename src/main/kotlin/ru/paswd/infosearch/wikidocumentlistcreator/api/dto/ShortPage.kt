package ru.paswd.infosearch.wikidocumentlistcreator.api.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class ShortPage (

    @JsonProperty("pageid")
    var pageId: Long?,

    @JsonProperty("title")
    var title: String?
)