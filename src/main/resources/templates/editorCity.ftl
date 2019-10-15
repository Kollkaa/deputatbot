<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">
<@c.page>
    <div>

            <#attempt>
                <div><a>-- Номер округа Міськ ради ${okrugcity.getNumber()}</a></div>
                <#recover>
            </#attempt>
            <#attempt>
             <form action="/okrugcity" method="post">
                <#list deputats.findAllByOkrugCity(okrugcity)as dep>
                    <input type="text" name="deputatname" value="${dep.getName()}">
                    <input type="text" name="deputatsurname" value="${dep.getSurname()}">
                    <input type="text" name="deputatpartional" value="${dep.getPartion()}">
                    <div>
                        <h6>Виберіть Партію</h6>
                        <select  name="partis">
                            Виберіть потрібний партію
                            <#list partias as partia >
                                <#if partia.GetPartiaName()==dep.getPartia()>
                                    <option value="${partia}" selected="selected">${partia.GetPartiaName()}</option>

                                <#else>
                                    <option value="${partia}">${partia.GetPartiaName()}</option>
                                </#if>
                            </#list>
                        </select>
                    </div>
                    <a href="/deputats/${dep.getId()?c}">Редагувати</a>
                </#list>
                <#if deputats.findAllByOkrugCity(okrugcity).size()==0>
                    <form method="post" action="/deputats/addDep">
                        <input type="text" name="name" value="name">
                        <input type="text" name="surname" value="surname">
                        <input type="text" name="partional" value="partion">
                        <div>
                            <h6>Виберіть Партію</h6>
                            <select  name="partia">
                                Виберіть потрібний партію
                                <#list partias as partia >
                                        <option value="${partia}">${partia.GetPartiaName()}</option>
                                </#list>
                            </select>
                        </div>
                        <input type="hidden" value="${okrugcity.getId()?c}" name="id">
                        <input type="hidden" value="${_csrf.token}" name="_csrf">
                        <button type="submit">Сохранить</button>
                    </form>

                </#if>
            <input type="hidden" value="${okrugcity.getId()?c}" name="okrugcityId">
            <input type="hidden" value="${_csrf.token}" name="_csrf">
            <button type="submit">Сохранить</button>
        </form>
                <#recover>
                    ew
                        <form method="post" action="/deputats/addDep">
                            <input type="text" name="name" value="name">
                            <input type="text" name="surname" value="surname">
                            <input type="text" name="partion" value="partion">
                            <div>
                                <h6>Виберіть Партію</h6>
                                <select  name="partia">
                                    Виберіть потрібний партію
                                    <#list partias as partia >

                                            <option value="${partia}">${partia.GetPartiaName()}</option>
                                    </#list>
                                </select>
                            </div>
                            <input type="hidden" value="${okrugcity.getId()?c}" name="id">
                            <input type="hidden" value="${_csrf.token}" name="_csrf">
                            <button type="submit">Сохранить</button>
                        </form>
            </#attempt>

    </div>

</@c.page>