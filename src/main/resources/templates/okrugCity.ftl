<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">
<@c.page>

    <div class="big-banner">

        <div class="form-row">
            <div class="form-group col-md-6">
                <form method="get" action="/okrugcity" class="form-inline">
                    <input type="number" name="number" class="form-control" value="${number?ifExists}" placeholder="Search by tag">
                    <button type="submit" class="btn btn-primary ml-2">Искать</button>
                </form>
            </div>
        </div>
        <div class="card-columns " >

            <form method="get" action="/okrugcity/getokrugs">
                <select name="city">
                    <#list cities as city>
                        <option value="${city.getName()}">${city.getName()}</option>
                    </#list>
                </select>

                <button type="submit">Вибрать</button>
            </form>

        </div>
    </div>
</@c.page>