Handlebars.registerHelper('test', function (context, options) {
    console.log(context);
    console.log(options);

});
console.log("asdsada");
Handlebars.registerHelper('trali', function () {
console.log("asdsada");
    return "tralli";
});



$(document).ready(function(){
var temp = $('#timeline').html();
var comp = Handlebars.compile(temp);
});

Handlebars.registerHelper('ifCond', function (v1, operator, v2, options) {

    switch (operator) {
        case '==':
            return (v1 == v2) ? options.fn(this) : options.inverse(this);
        case '===':
            return (v1 === v2) ? options.fn(this) : options.inverse(this);
        case '!=':
            return (v1 != v2) ? options.fn(this) : options.inverse(this);
        case '!==':
            return (v1 !== v2) ? options.fn(this) : options.inverse(this);
        case '<':
            return (v1 < v2) ? options.fn(this) : options.inverse(this);
        case '<=':
            return (v1 <= v2) ? options.fn(this) : options.inverse(this);
        case '>':
            return (v1 > v2) ? options.fn(this) : options.inverse(this);
        case '>=':
            return (v1 >= v2) ? options.fn(this) : options.inverse(this);
        case '&&':
            return (v1 && v2) ? options.fn(this) : options.inverse(this);
        case '||':
            return (v1 || v2) ? options.fn(this) : options.inverse(this);
        default:
            return options.inverse(this);
    }
});

<!--
Handlebars.registerHelper('user', function(userinfo, param) {
        for(var i=0; i < userinfo.length; i++) {
            if
        }
return person.firstName + " " + person.lastName;
});
-->