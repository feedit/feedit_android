// webview demo
'use strict';

/**
 * mock data
 */

var line = 20;
var data = [];
while (line --) {
  data.push({
    id: 1,
    title: '这里的一行文字是文章标题',
    url: 'http://www.xdf.me',
    read: false
  });
}

if (typeof dataFromInterface !== 'undefined') {
  data = dataFromInterface.get('data');
  data = JSON.parse(data);
}

;(function(global, undefined) {
  var $ = document.querySelectorAll.bind(document);
  var content = $('#content')[0];
  var tplElm = $('#template')[0].innerHTML;
  var tpl = grace.compile(tplElm);
  var node = tpl({
    list: data
  });
  content.innerHTML = node;
})(this);

