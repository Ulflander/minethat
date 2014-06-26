

(function () {
    "use strict";

    function qualifyURL(url){
        var a = document.createElement('a');
        a.href = url; // set string url
        url = a.href; // get qualified url
        return url;
    }

    function domain(){
        var a = document.createElement('a');
        a.href = '/';
        return a.hostname;
    }

    function path(){
        var a = document.createElement('a');
        a.href = '';
        return a.pathname;
    }

    /**
     * Send page source and description to popup.
     */
    chrome.extension.sendMessage({
        action: "getSource",

        domain: domain(),

        valid: document.getElementsByTagName('title').length > 0
                || document.getElementsByTagName('rss').length > 0,

        meta: (function () {
            // Check for title (mandatory)
            if (document.getElementsByTagName('title').length === 0) {
                return;
            }

            var metas = document.getElementsByTagName('meta'),
                links = document.getElementsByTagName('link'),
                o,
                i,
                l = metas.length,
                url,
                found = [],
                notFound = function(u) {
                    return found.indexOf(u) === -1;
                },
                result = {
                    title: document.getElementsByTagName('title')[0].innerText,
                    feeds: {}
                };

            for (i = 0; i < l; i += 1) {
                o = metas[i];
                if (o.getAttribute('name') === 'twitter:title') {
                    result.title = o.getAttribute('content');
                } else if (o.getAttribute('property') === 'og:title') {
                    result.title = o.getAttribute('content');
                }
            }

            l = links.length;
            for (i = 0; i < l; i += 1) {
                o = links[i];
                if (o.getAttribute('rel') === 'alternate'
                    && o.getAttribute('type') === 'application/rss+xml'
                    && notFound(url)) {
                    url = qualifyURL(o.getAttribute('href'));
                    result.feeds[o.getAttribute('title') || url] = url;
                    found.push(url);
                }
            }

            links = document.getElementsByTagName('a');

            l = links.length;
            for (i = 0; i < l; i += 1) {
                o = links[i];
                url = qualifyURL(o.getAttribute('href'));
                if (((url.indexOf('rss') > -1 && url.indexOf('xml') > -1)
                    || (url.indexOf('/feed.') > -1
                        || url.indexOf('/feeds.') > -1)
                    || (url.indexOf('/feed/') > -1
                        || url.indexOf('/feeds/') > -1))
                     && notFound(url)) {

                    found.push(url);
                    result.feeds[o.getAttribute('title') ||
                                o.innerText || url] = url;
                }
            }

            // Special cases
            if (domain() === 'medium.com') {
                url = path().split('/')[1];
                if (!!url && notFound(url)) {
                    result.feeds[result.title] =
                    'https://medium.com/feed/' + url;
                    found.push(url);
                }
            }

            return result;
        }()),

        /**
         * Extract page source
         *
         * @author Rob W <http://stackoverflow.com/users/938089/rob-w>
         */
        source: (function (document_root) {
            var html = '',
                node = document_root.firstChild;
            while (node) {
                switch (node.nodeType) {
                    case Node.ELEMENT_NODE:
                        html += node.outerHTML;
                        break;
                    case Node.TEXT_NODE:
                        html += node.nodeValue;
                        break;
                    case Node.CDATA_SECTION_NODE:
                        html += '<![CDATA[' + node.nodeValue + ']]>';
                        break;
                    case Node.COMMENT_NODE:
                        // html += '<!--' + node.nodeValue + '-->';
                        // Skip it for Minethat
                        break;
                    case Node.DOCUMENT_TYPE_NODE:
                        // (X)HTML documents are
                        // identified by public identifiers
                        html += "<!DOCTYPE " + node.name +
                            (node.publicId ?
                                ' PUBLIC "' + node.publicId + '"' :
                                '') +
                            (!node.publicId && node.systemId ?
                                ' SYSTEM' :
                                '') +
                            (node.systemId ?
                                ' "' + node.systemId + '"' :
                                '') +
                            '>\n';
                        break;
                }

                node = node.nextSibling;
            }

            return html;
        }(document))

    });

}());