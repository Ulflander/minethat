

(function () {
    "use strict";

    function qualifyURL(url){
        var a = document.createElement('a');
        a.href = url; // set string url
        url = a.href; // get qualified url
        return url;
    }
console.log('checking for hunk', window);
    if (!!window.hunk) {
        console.log('injected hunk conf');
        window.hunk.conf('minethat_extension_installed', true);
    }

    /**
     * Send page source and description to popup.
     */
    chrome.extension.sendMessage({
        action: "getSource",

        meta: (function () {
            var metas = document.getElementsByTagName('meta'),
                links = document.getElementsByTagName('link'),
                o,
                i,
                l = metas.length,
                url,
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
                    && o.getAttribute('type') === 'application/rss+xml') {
                    url = qualifyURL(o.getAttribute('href'));
                    result.feeds[o.getAttribute('title') || url] = url;
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