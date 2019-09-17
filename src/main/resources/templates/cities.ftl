<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">
<@c.page>

    <div class="big-banner">
        <div class="card-columns " >
            <#list cities as citi>
                <div class="card my-3 " >
                    <div class="m-2">
                        <#attempt>
                        <a>${citi.getMer().getName()}</a>
                        <a>${citi.getMer().getSurname()}</a>
                        <a>${citi.getMer().getPartion()}</a>
                            <#recover>
                                okrug haven`t choose
                        </#attempt>
                        <div>
                            <#attempt>
                                <a>${citi.name+" "+ citi.getTypeCity()}</a>

                                <td><a href="/cities/${citi.getId()?c}">edit</a></td>

                                <#recover>
                                    okrug haven`t choose
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