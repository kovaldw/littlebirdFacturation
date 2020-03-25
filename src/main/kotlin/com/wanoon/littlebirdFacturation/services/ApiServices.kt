package com.wanoon.littlebirdFacturation.services

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import com.wanoon.littlebirdFacturation.extensions.DEBUT_PAGE
import com.wanoon.littlebirdFacturation.extensions.NB_ELEMENTS_PAR_PAGE


@Configuration
class ApiServices
{

    @Bean
    fun getInstancesApiServices():ApiServices
    {
        return ApiServices()
    }

    fun getParametersFromGetRequest(queryMap: MutableMap<String, Array<String>>): HashMap<String, String>
    {
        var finalParameters = HashMap<String, String>()
        try {
            for (element in queryMap)
            {
                finalParameters[element.key] = element.value[0]
            }
        }
        catch (e:Exception)
        {

        }


        return finalParameters
    }

    fun getNumeroPageFromParameters(parameters: HashMap<String, String>): Int
    {
        var numeroPage = DEBUT_PAGE;
        try {
            if (parameters.containsKey("page")) numeroPage = parameters["page"].toString().toInt()
        }
        catch (e:Exception)
        {

        }
        return numeroPage
    }

    fun getNbElementsFromParameters(parameters: HashMap<String, String>): Int
    {
        var nbElements = NB_ELEMENTS_PAR_PAGE;
        try {
            if (parameters.containsKey("nbElements")) nbElements = parameters["nbElements"].toString().toInt()
        }
        catch (e:Exception)
        {

        }

        return nbElements
    }


}