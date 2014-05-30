'use strict';

/* Controllers */
var stData;
var denomw = {
    'hun':'Hundred',
   'th' : 'Thousand',
    'mill': 'Million',
    'billl': 'Billion'
};

function mainCtrl($scope,$http){  

    splashIt(true);
    var curl = getContext()+'/ramap-service/DataProvider?title='+"Disaggregated Budget of Ministry of Works for Road Construction Per LGA"+'&year='+'2012';
    console.log(curl);                
    retrieveDataset($scope,$http,curl);                              
    
    
    $scope.changeMap = function(){           
        splashIt(true);
        var curl = getContext()+'/ramap-service/DataProvider?title='+$scope.title+'&year='+$scope.year;
        console.log(curl);                
        retrieveDataset($scope,$http,curl);                              
    };
     
    function retrieveDataset($scope,$http, curl){
     $http.get(curl).success(function (data,status){        
         console.log(data.description);          
         if(!data){
             alert("Not available");
             splashIt(false);
             return;
         }
         document.getElementById('desc').innerHTML=data.description;
         document.getElementById('title').innerHTML=data.title;         
         loadColors(data.results);
         stData=data.results;
         handleKeys($scope,data.results); 
         splashIt(false);
     }).error(function(data,status){
         console.log(data);
     });
 }
                  
}

function loadColors(data){
    var paths = document.getElementsByTagName('path');
    for(var i=0;i<data.length;i++){        
        for(var j=0;j<paths.length;j++){
            var path = paths[j];
            if(data[i][4]===path.id){                 
                path.style.fill=getColor(data[i][5],data);                
                break;
            }
        }
    }
}

function getColor(amount, data){
    var color;    
    var segments = segmentThem(data);
    if(amount<=segments[1]){
        color = '#FFE5E9';
    }else if(amount<segments[2]){
        color = '#FFB1BC';
    }else if(amount < segments[3]){
        color = '#FF657B';
    }else if(amount < segments[4]){
        color = 'FE0024';
    }else if (amount<segments[5]){
        color = '#B20019';
    }else if (amount <segments[6]){
        color = '#7E0012';
    }else{
        color = '#4C000B';
    }
    return color;              
}

function segmentThem(data){
    var min = findMin(data);
    var max = findMax(data);
    var diff = max - min;
    var interval =diff/7;    
    interval = Math.round(interval);
    interval = roundSpecially(interval);    
    var amounts = new Array();
    for(var i=0;i<7;i++){
        if(i===0){
            amounts[i]=min;
            continue;
        }
        var prev = parseInt(i)-1;
        amounts[i] = amounts[prev]+interval;
        
    }
    return amounts;
}

function findMin(data){  
    var least =Number(data[0][5]);
    for(var i=0;i<data.length;i++){
        var num = Number(data[i][5]);
        if(num<least){
            least=num;
        }
    }
    return least;
}

function findMax(data){
    var max = Number(data[0][5]);
    for(var i=0;i<data.length;i++){
        var num = Number(data[i][5]);
        if(num>max){
            max=num;
        }
    }
    return max;
}

function handleKeys($scope,data){
    var segments = segmentThem(data);
    var result = new Array();
    var denom;
    var denomVal=0;
    if((segments[4]/1000000000)>0.99){
        denom = denomw.billl;
        denomVal = 1000000000;
    }else if ((segments[4]/1000000)>0.99){
        denom = denomw.mill;
        denomVal = 1000000;
    }else if((segments[4]/1000)>0.99){
        denom = denomw.th;
        denomVal = 1000;
    }else{
        denom = denomw.hun;
        denomVal = 100;
    }
    for(var i=0; i<segments.length;i++){
        result[i] = segments[i]/denomVal;
    }           
    document.getElementById("denom").innerHTML=' (=N= '+denom+')';
    document.getElementById("1c").innerHTML=result[0]+'-'+result[1];
    document.getElementById("2c").innerHTML=result[1]+'-'+result[2];
    document.getElementById("3c").innerHTML=result[2]+'-'+result[3];
    document.getElementById("4c").innerHTML=result[3]+'-'+result[4];
    document.getElementById("5c").innerHTML=result[4]+'-'+result[5];
    document.getElementById("6c").innerHTML=result[5]+'-'+result[6];
    document.getElementById("7c").innerHTML='Above '+result[6];
}

function roundSpecially(num){
    num = num+'';
    var length = num.length-1;
    var re = num[0];
    for(var i=0; i<length;i++){
        re=re+'0';
    }
    return Number(re);
}

function diffFormatter(val){
    if(!val)
        return 'Not available';
    if(parseFloat(val)>0){
        return val+'% Up';
    }else if(parseFloat(val)===0){
        return val+'%';
    }else{        
        val = (-1*parseFloat(val));        
        return val+'% Down';
    }    
}
function getData(id){  
    var result;
    for(var i=0;i<stData.length;i++){
        if(id===stData[i][4]){
            result = stData[i];                        
        }
    }
    if(typeof result === 'undefined'){
        return alert("This program has some undefined values on line 224");
    }else{
        return result;
    }  

}

function currency(sSymbol, vValue) {
   vValue = Number(vValue);
   var aDigits = vValue.toFixed(2).split(".");
   aDigits[0] = aDigits[0].split("").reverse().join("").replace(/(\d{3})(?=\d)/g, "$1,").split("").reverse().join("");
   return sSymbol + aDigits.join(".");
}

function showDialog (){
    var el = document.getElementById("myOverlay");
    el.style.visibility = (el.style.visibility==='hidden')?'visible':'hidden';   
}
function splashIt(flag){    
     var el = document.getElementById("loading-overlay");
    el.style.visibility = (flag)?'visible':'hidden';
}

function getContext(){    
    var arr = document.URL.split('/',10);
    var context = arr[0]+'//'+arr[2];
    return context;
}

    