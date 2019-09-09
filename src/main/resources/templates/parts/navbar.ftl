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
                <a class="nav-link" href="/">Главная</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/main">Сообщения</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/deputats">Депутати по округам</a>
            </li>

            <li class="nav-item">
                <a class="nav-link" href="/mer">Мери по містам</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/okrugndu">Округа НДУ</a>
            </li>
             <li class="nav-item">
               <a class="nav-link" href="/okrugcity">Округа Міських рад</a>
             </li>
             <li class="nav-item">
                <a class="nav-link" href="/okrugobl">Округа обласної ради</a>
             </li>
            <li class="nav-item">
                <a class="nav-link" href="/dilniziaf">Дільниці</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/editer">Дільниці</a>
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