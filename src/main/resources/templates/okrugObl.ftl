<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">
<@c.page>

    <div class="big-banner">
        <div class="card-columns " >
            <#list okrugs as okrug>
                <div class="card my-3 " >
                    <div class="m-2">
                        <a>${okrug.number}</a>

                        <#list repoDep.findAllByOkrugObl(okrug) as deputat>
                       <div> <a>${deputat.name}</a>
                        <a>${deputat.surname}</a>
                        <a>${deputat.partion}</a>
                       </div>
                       </#list>
                        <div>
                            <#attempt>
                                <h1>${okrug.number}</h1>
                                <td><a href="/okrugobl/${okrug.getId()?c}">edit</a></td>

                                <#recover>
                                    okrug haven`t choose
                            </#attempt>
                        </div>
                    </div>
                    <div class="card my-1">
                        <a>${okrug.getRegion()}</a>
                    </div>

                </div>

            <#else>
                No message
            </#list>
        </div>
    </div>
</@c.page>