

var currentLang;
var currentArticle;
var langsOnServer;

function firstDate(data) {
	var first = new Date().getTime();
    $(data).each(function(i, item) {
         if(item.t < first) {
         	first = item.t;
         }
    });
    return first;
}

function activityAtTime(time, data) {
	var activity = 0;
	$(data).each(function(i, item) {
    	if(item.t == time) {
    		activity += item.a;
    	}
    })
    //TODO: return null if the dump is not loaded
    return activity;
}

function hourAvg(data) {
	function zeroArray() {
		var ret = new Array();
		for(i = 0; i <= 23; i++) {
			ret[i] = 0;
		}
		return ret;
	}
	var sums = zeroArray();
	var cnts = zeroArray();

	hourCnts = new Array();
    for(i = 0; i <= 23; i++) {
    	hourCnts[i] = 0;
	}
	$(data).each(function(idx, item){
	     var h = new Date(item.t).getHours();
	     hourCnts[h]++;
	});


    for(i = 0; i <= 23; i++) {
        $(data).each(function(idx, item){
			if(new Date(item.t).getHours() == i) {
        		sums[i] += item.a;
        	}
        });
    }

	var ret = new Array();
	$(sums).each(function(hour, sum) {
		ret[hour] = sum / hourCnts[hour]; //cnts
	});

	return ret;
}

function flatAvg(data) {
    var first = firstDate(data);
    var i = new Date(first);
    var sum = 0;
    var cnt = 0;

	$(data).each(function(item, i){
		sum += i.a;
	})

    while(i.getTime() < new Date().getTime()) {
        i.setTime(i.getTime() + 60 * 60 * 1000);
        cnt++;
    }
	return (cnt > 0) ? sum / cnt : 0;
}

function updateChart() {
      $('#chart_div').fadeOut(300);
      if(window.location.hash) {
           $.get('s/pageactivity/' + currentLang + "/" + currentArticle, function(data) {
              var proc = new Array();
              proc[0] = ["Date", "activity", "avg(t)", "avg"];

              var d = new Date(firstDate(data));
              var avg = flatAvg(data);
              var hAvg = hourAvg(data);
              while(d.getTime() < new Date().getTime()) {
              	d.setHours(d.getHours() + 1, 0, 0, 0);
              	proc[proc.length]=[
              		d.toUTCString(),
              		activityAtTime(d.getTime(), data),
              		hAvg[d.getHours()],
              		avg];
              }

              var chart = new google.visualization.LineChart(document.getElementById('articleActivity'));

              var options = {
                   "title" : currentArticle+" ["+currentLang+"]",
                   "series" : chartSeries,
                   "hAxis" : {
	                   "format" : 'mmm dd yy'
                   }
              };
              chart.draw(google.visualization.arrayToDataTable(proc), options);
              $('#articleActivity').fadeIn(300);
           })
      }
}

function updateVersion() {
	$.get('s/version', function(data) {
		$('#version').text(data);
	});
}

function updateLangs() {
    $.get('s/languages/', function(data) {
	//just for the case
    $('#langs').empty();
    langsOnServer = data;
    $(data).each(function(idx, lang) {
    	var dom = $('<input>')
    		.attr('type','radio')
    		.attr('name','langs')
    		.attr('id', 'lang-'+lang)
    		.attr('value',lang)
    		.click(function() {
    			currentLang = lang;
                updateHash();
				updateChart();
				updateLangLinks();
				updateTotals();
    		});
    	if(lang == currentLang) {
    		$(dom).attr('checked','true');
    	}
        $('#langs').append(dom);
        $('#langs').append($('<label>').attr('for','lang-'+lang).text(lang));
    })
    $('#langs').buttonset();
    if($('#pageName').val() != '') {
    	updateLangLinks();
    }
    });
}

function updateLangLinks() {
	$('#langlinks').empty();
	$.get('s/langlinks/' + currentLang + '/' + currentArticle, function(data) {
		$.each(data, function(lang, articleInLang) {
        	var link = $('<a>').text(lang + ': ' + articleInLang).addClass('langlink');
            if($.inArray(lang, langsOnServer) != -1) {
        		$(link).addClass('enabled').click(function() {
        			currentLang = lang;
                    currentArticle = articleInLang;
                    updateHash();
                    updateChart();
                    updateTotals();
                    $('#pageName').val(currentArticle);
                    //$('#langs input[checked="checked"]').removeAttr('checked');
                    $('#lang-'+currentLang).attr('checked','checked').button('refresh');
        		});
        	} else {
                $(link).addClass('disabled');
        	}
			$('#langlinks').append(link);
        });
    });
}

function updateTotals() {
	$.get('s/stats/' + currentLang, function(data) {
        var proc = new Array();
        proc[0] = ["Date", "activity", "avg(t)", "avg"];

        var d = new Date(firstDate(data));
        var avg = flatAvg(data);
        var hAvg = hourAvg(data);
        while(d.getTime() < new Date().getTime()) {
        	d.setHours(d.getHours() + 1, 0, 0, 0);
            proc[proc.length]=[
            	d.toUTCString(),
            	activityAtTime(d.getTime(), data),
            	hAvg[d.getHours()],
            	avg];
            }

            var chart = new google.visualization.LineChart(document.getElementById('totals'));

            var options = {
                        title : " ["+currentLang+"]",
                        "series" : chartSeries
                      };
            chart.draw(google.visualization.arrayToDataTable(proc), options);
            $('#totals').fadeIn(300);

	});
}

$(document).ready(updateLangs)

function languageChanged() {
	//TODO
	//update hash
	//update chart
	updateChart();
	updateLangLinks();
	updateTotals();
}

function updateHash() {
	window.location.hash =  "#" + currentLang + '/' + currentArticle;
}

$(document).ready(function(){

	updateVersion();
	currentLang = window.location.hash.replace(/\/.*/,'').replace('#','');
    currentArticle = window.location.hash.replace(/..\//,'').replace('#','');

    updateTotals();

	$('#pageName').val(currentArticle);
	var changeEvent = function(event, ui) {
                              	currentArticle = $('#pageName').val();
                              	updateHash();
                              	updateChart();
                              	updateLangLinks();
                              };
    $('#pageName').autocomplete({
    	source : function(request, response) {
    	    $.get('s/pageautocomplete?lang='+currentLang+'&term='+request.term, response);
    	}, //"s/pageautocomplete?lang=hu",
    	delay  : 600,
        minLength : 3,
        change : changeEvent,
        select : changeEvent
    })
});

