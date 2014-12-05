// webview demo
'use strict';

/**
 * mock data
 */
var line = 10;
var data = [];

while (line --) {
  data.push({
    id: line,
    title: '这里的一行文字是文章标题长长长长长长长长长长长长',
    url: 'http://www.xdf.me',
    date: '2013.12.12'
  });
}

var userAgent = navigator.userAgent;
var ios = !!userAgent.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/i);
var android = userAgent.indexOf("Android") > -1 || userAgent.indexOf("Linux") > -1;

var exec = function(data, global) {
  var $ = document.querySelectorAll.bind(document);
  var content = $('#content')[0];
  var tplElm = $('#template')[0].innerHTML;
  var tpl = grace.compile(tplElm);
  var API = 'http://xudafeng.com/feedit/read.php';
  var node = tpl({
    list: data
  });
  content.innerHTML = node;
  document.addEventListener('click', function(e) {
    var target = e.target;
    if (target.nodeName === 'BUTTON') {
      var id = target.id.split('button-')[1];
      id = parseInt(id);
      var url = target.getAttribute('data-url');
      var title = target.getAttribute('data-title');
      //ajax
      JSONP(API, {
        id: id
      }, 'callback', function(data) {
        if (data) {
          if (data.success) {
            target.style.display = 'none';
            global.targetTitle = title;
            global.targetUrl = url;
            location.href = url;
          }
        }
      });
    }
  }, false);
}

if (ios) {
  this.Ready = function(data) {
    exec(data, window);
  };
} else if(android) {
  data = dataFromInterface.get('data');
  data = JSON.parse(data);
  exec(data, this);
} else {
  exec(data, this);
}

