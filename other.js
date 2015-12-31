$(document).ready(function () {
    $.ajaxSetup({
        cache: false,
    });
});

// Prevent the backspace key from navigating back.
$(document).unbind('keydown').bind('keydown', function (event) {
    var doPrevent = false;
    if (event.keyCode === 8) {
        var d = event.srcElement || event.target;
        if ((d.tagName.toUpperCase() === 'INPUT' && 
             (
                 d.type.toUpperCase() === 'TEXT' ||
                 d.type.toUpperCase() === 'PASSWORD' || 
                 d.type.toUpperCase() === 'FILE' || 
                 d.type.toUpperCase() === 'SEARCH' || 
                 d.type.toUpperCase() === 'EMAIL' || 
                 d.type.toUpperCase() === 'NUMBER' || 
                 d.type.toUpperCase() === 'DATE' )
             ) || 
             d.tagName.toUpperCase() === 'TEXTAREA') {
            doPrevent = d.readOnly || d.disabled;
        }
        else {
            doPrevent = true;
        }
    }

    if (doPrevent) {
        event.preventDefault();
    }
});

var isIE8 = window.isIE_Eight;
if (!isIE8) {
    localStorage.clear();
}
(function () {
    /**
     * Decimal adjustment of a number.
     *
     * @param	{String}	type	The type of adjustment.
     * @param	{Number}	value	The number.
     * @param	{Integer}	exp		The exponent (the 10 logarithm of the adjustment base).
     * @returns	{Number}			The adjusted value.
     */
    function decimalAdjust(type, value, exp) {
        // If the exp is undefined or zero...
        if (typeof exp === 'undefined' || +exp === 0) {
            return Math[type](value);
        }
        value = +value;
        exp = +exp;
        // If the value is not a number or the exp is not an integer...
        if (isNaN(value) || !(typeof exp === 'number' && exp % 1 === 0)) {
            return NaN;
        }
        // Shift
        value = value.toString().split('e');
        value = Math[type](+(value[0] + 'e' + (value[1] ? (+value[1] - exp) : -exp)));
        // Shift back
        value = value.toString().split('e');
        return +(value[0] + 'e' + (value[1] ? (+value[1] + exp) : exp));
    }

    // Decimal round
    if (!Math.round10) {
        Math.round10 = function (value, exp) {
            return decimalAdjust('round', value, exp);
        };
    }
    // Decimal floor
    if (!Math.floor10) {
        Math.floor10 = function (value, exp) {
            return decimalAdjust('floor', value, exp);
        };
    }
    // Decimal ceil
    if (!Math.ceil10) {
        Math.ceil10 = function (value, exp) {
            return decimalAdjust('ceil', value, exp);
        };
    }

})();
function decimalPlaces_roundN(number, precision) {

    var result = (Math.round10(parseFloat(number), (precision * -1)) * 1).toString();
    var test = result;
    if (test.toString().replace(/^-?\d*\.?|0+$/g, '').length < precision) {
        result = Math.round10(parseFloat(result), (precision * -1));
        result = parseFloat(result).toFixed(precision);
    }

    return result;// to string removes trailing 0s from the end of a decimal representatil of a number
}

function addCommasToAmount(str) {
    str = str.replace(/\B(?=(?:\d{3})+(?!\d))/g, ',')
    return str;
}

function convertToLocalDate(dateString) {        
    
    var dateTimeSplit = dateString.split(" ");
    var dateString = (dateTimeSplit[0])?dateTimeSplit[0]:"0000-00-00";
    var timeString = (dateTimeSplit[1])?dateTimeSplit[1]:"00:00:0000.0";
    timeString = (timeString.split(".")[0])?timeString.split(".")[0]:"00:00:0000";
    
    var dd = (dateString.split("-")[2])?dateString.split("-")[2]:"00";    
    var mm = (dateString.split("-")[1])?dateString.split("-")[1]:"00";
    var yyyy = (dateString.split("-")[0])?dateString.split("-")[0]:"0000";    
    dateString = mm +  "/" + dd + "/" + yyyy + " " + timeString + " EST";
    
    var date = new Date(dateString);//'6/29/2011 4:52:48 PM UTC'    
    var timezone = (date.toString().split("(")[1])? "(" + date.toString().split("(")[1]:"()";
    dateString = date.getFullYear() +"-"+ (Number(date.getMonth()) + 1) +"-"+ date.getDate() + " " + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds() + " " + timezone;
    
    
    return dateString;
//    return date.toString();   
}
