$(function() {
	$("#top-nav").append('\
        <li>\
            <a href="queries.php?proc=list">\
                <span class="nav-list-item-content">Queries</span>\
                <span class="drop-down"></span>   \
            </a>\
        </li>\
        <li>\
            <a href="visuals.php?proc=list">\
                <span class="nav-list-item-content">Visuals</span>\
            </a>\
        </li>\
        <li>\
        <a href="dashboards.php?proc=list">\
                <span class="nav-list-item-content">Dashboards</span>\
            </a>\
        </li>\
	');
    $("span.nav-list-item-content:contains("+$('meta[name=page]').attr('content')+")").closest("li").addClass("active");
});