<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">
<@c.page>

    <div class="big-banner">
        <div class="form-row">
            <div class="form-group col-md-6">
                <form method="get" action="/mer" class="form-inline">
                    <input type="text" name="filter" class="form-control" value="${filter?ifExists}" placeholder="Search by tag">
                    <button type="submit" class="btn btn-primary ml-2">Искать мера</button>
                </form>
            </div>
        </div>


        <div class="collapse big-banner"  style="height: 500px ;width: 250px;" id="collapseExample">
            <div class="form-group mt-3">
                <form method="post"  action="/add" >
                    <div class="form-group">
                        <input type="text" class="form-control" name="text" placeholder="Введите сообщение" />
                    </div>
                    <div class="form-group">
                        <input type="text" class="form-control" name="tag" placeholder="Тэг">
                    </div>

                    <input type="hidden" name="_csrf" value="${_csrf.token}" />
                    <div class="form-group">
                        <button type="submit" class="btn btn-primary">Добавить</button>
                    </div>
                </form>
            </div>
        </div>

        <div class="card-columns " >
            <#list mers as mer>
                <div class="card my-3 " >
                    <div class="m-2">
                        <a>${mer.surname}</a>
                        <span>${mer.name}</span>
                        <i>${mer.partion}</i>
                    </div>
                    <div class="card-footer text-muted">
                        ${mer.city.name}
                        ${mer.city.getTypeCity()}
                    </div>

                </div>

            <#else>
                No message
            </#list>
        </div>
    </div>
</@c.page>