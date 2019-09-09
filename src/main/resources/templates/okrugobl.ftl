<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">
<@c.page>

    <div class="big-banner">
        <div class="form-row">
            <div class="form-group col-md-6">
                <form method="get" action="/okrugobl" class="form-inline">
                    <input type="text" name="filter" class="form-control" value="${filter?ifExists}" placeholder="Search by tag">
                    <button type="submit" class="btn btn-primary ml-2">Искать</button>
                </form>
            </div>
        </div>



        <div class="card-columns " >
            <#list okrugs as okrug>
                <div class="card my-3 " >
                    <div class="m-2">
                        <span>${okrug.number}</span>
                        <i>${okrug.deputat.surname?ifExists}</i>
                    </div>
                    <div class="card-footer text-muted">
                        ${okrug.region?ifExists}
                    </div>

                </div>

            <#else>
                No message
            </#list>
        </div>
    </div>
</@c.page>