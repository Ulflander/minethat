

(function () {
    "use strict";

    /**
     * Send page source and description to popup.
     */
    chrome.extension.sendMessage({
        action: "getSource",

        meta: (function () {
            var metas = document.getElementsByTagName('meta'),
                links = document.getElementsByTagName('link'),
                i,
                l = metas.length,
                result = {};

            for (i = 0; i < l; i += 1) {
                console.log(metas[i]);
            }

            l = links.length;
            for (i = 0; i < l; i += 1) {
                console.log(links[i]);
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
            console.log(node);
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