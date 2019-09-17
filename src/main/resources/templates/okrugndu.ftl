<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">
<@c.page>

    <div class="big-banner">
            <div class="card-columns " >
            <#list okrugs as okrug>
                <div class="card my-3 " >
                    <div class="m-2">
                        <a>${okrug.number}</a>
                        <a>${okrug.deputat.name}</a>
                        <a>${okrug.deputat.surname}</a>
                        <a>${okrug.deputat.partion}</a>
                        <div>
                        <#attempt>
                            <h1>${okrug.number}</h1>
                            <td><a href="/okrugndu/${okrug.getId()?c}">edit</a></td>

                            <#recover>
                                <h1>${okrug.number}</h1>
                                <td><a href="/okrugndu/${okrug.getId()}">edit</a></td>
                        </#attempt>
                        </div>
                    </div>


                </div>

            <#else>
                No message
            </#list>
        </div>
    </div>
</@c.page>