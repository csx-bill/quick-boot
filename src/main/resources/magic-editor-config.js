var MAGIC_EDITOR_CONFIG = {
    request: {
        beforeSend: function(config){
            config.headers.token = getToken('accessToken');
            return config;
        }
    },
    getMagicTokenValue: function(){
        return getToken('accessToken');
    }
}

function getToken(cname)
{
    var token = localStorage.getItem(cname);
    if (token) {
        return token;
    }
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for(var i=0; i<ca.length; i++)
    {
        var c = ca[i].trim();
        if (c.indexOf(name)===0) return c.substring(name.length,c.length);
    }
    return "";
}