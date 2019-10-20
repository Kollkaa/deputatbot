<#include "security.ftl">
<#import "login.ftl" as l>

<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <a class="navbar-brand" href="/">Project Lions</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">

            <li class="nav-item">
                <a class="nav-link" href="/deputats">Депутати по округам</a>
            </li>

            <li class="nav-item">
                <a class="nav-link" href="/cities">Мери міст та голови ОТГ</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/okrugndu">НДУ</a>
            </li>
             <li class="nav-item">
               <a class="nav-link" href="/okrugcity">Міськради та ради ОТГ</a>
             </li>
             <li class="nav-item">
                <a class="nav-link" href="/okrugobl">Обласні ради</a>
             </li>
            <li class="nav-item">
                <a class="nav-link" href="/dilnizias">Дільниці</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/feedback">Відгуки</a>
            </li>


            <#if isAdmin>
                <li class="nav-item">
                    <a class="nav-link" href="/user">Пользователи</a>
                </li>

            </#if>
        </ul>

        <div class="navbar-text mr-3">${name}</div>
        <@l.logout />
    </div>
</nav>