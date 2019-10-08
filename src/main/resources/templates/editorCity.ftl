<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">
<@c.page>
    <div>
        <form action="/okrugcity" method="post">
            <#attempt>
                <div><a>-- Номер округа НДУ ${okrugcity.getNumber()}</a></div><
                <#recover>

            </#attempt>
            <#list deputats.findAllByOkrugCity(okrugcity)as dep>
                <input type="text" name="deputatname" value="${dep.getName()}">
                <input type="text" name="deputatsurname" value="${dep.getSurname()}">
                <input type="text" name="deputatpartional" value="${dep.getPartion()}">
                <div>
                    <h6>Виберіть Партію</h6>
                    <select  name="partis">
                        Виберіть потрібний партію
                        <#list partias as partia >
                            <#if partia.GetPartiaName()==okrugobl.getDeputat().getPartia()>
                                <option value="${partia}" selected="selected">${partia.GetPartiaName()}</option>

                            <#else>
                                <option value="${partia}">${partia.GetPartiaName()}</option>
                            </#if>
                        </#list>
                    </select>
                </div>
            </#list>



            <input type="hidden" value="${okrugcity.getId()?c}" name="okrugcityId">
            <input type="hidden" value="${_csrf.token}" name="_csrf">
            <button type="submit">Сохранить</button>
        </form>
    </div>

</@c.page>