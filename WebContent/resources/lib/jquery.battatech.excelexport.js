/*
 * jQuery Client Side Excel Export Plugin Library
 * http://www.battatech.com/
 *
 * Copyright (c) 2013 Batta Tech Private Limited
 * Licensed under https://github.com/battatech/battatech_excelexport/blob/master/LICENSE.txt
 */

(function ($) {
    var $defaults = {
        containerid: null
        , datatype: 'table'
        , dataset: null
        , columns: null
        , returnUri: false
        , worksheetName: "My Worksheet"
        , encoding: "utf-8"
		, reportName: null
		, fileName: null
    };

    var $settings = $defaults;

	var reportHead;
	var fileName;
	
    $.fn.battatech_excelexport = function (options) {
        $settings = $.extend({}, $defaults, options);

        var gridData = [];
        var excelData;

        return Initialize();

        function Initialize() {
            var type = $settings.datatype.toLowerCase();
			reportHead = $settings.reportName;
			fileName = $settings.fileName;
            BuildDataStructure(type);

            switch (type) {
                case 'table':
                    excelData = Export(ConvertFromTable());
                    break;
                case 'json':
                    excelData = Export(ConvertDataStructureToTable());
                    break;
                case 'xml':
                    excelData = Export(ConvertDataStructureToTable());
                    break;
                case 'jqgrid':
                    excelData = Export(ConvertDataStructureToTable());
                    break;
            }

            if ($settings.returnUri) {
                return excelData;
            }
            else {
                //window.open(excelData);
				var blob=new Blob([excelData]);
				var link=document.createElement('a');
				link.href=window.URL.createObjectURL(blob);
				if(fileName==null){
				link.download="myFileName.xls";
				}else{
				link.download=fileName+".xls";
				}
				link.click();
            }
        }

        function BuildDataStructure(type) {
            switch (type) {
                case 'table':
                    break;
                case 'json':
                    gridData = $settings.dataset;
                    break;
                case 'xml':
                    $($settings.dataset).find("row").each(function (key, value) {
                        var item = {};

                        if (this.attributes != null && this.attributes.length > 0) {
                            $(this.attributes).each(function () {
                                item[this.name] = this.value;
                            });

                            gridData.push(item);
                        }
                    });
                    break;
                case 'jqgrid':
                    $($settings.dataset).find("rows > row").each(function (key, value) {
                        var item = {};

                        if (this.children != null && this.children.length > 0) {
                            $(this.children).each(function () {
                                item[this.tagName] = $(this).text();
                            });

                            gridData.push(item);
                        }
                    });
                    break;
            }
        }

        function ConvertFromTable() {
            var result = $('<div>').append($('#' + $settings.containerid).clone()).html();
            return result;
        }

        function ConvertDataStructureToTable() {
            var result = "<table>";

            result += "<thead><tr>";
            $($settings.columns).each(function (key, value) {
                if (this.ishidden != true) {
                    result += "<th";
                    if (this.width != null) {
                        result += " style='width: " + this.width + "'";
                    }
                    result += ">";
                    result += this.headertext;
                    result += "</th>";
                }
            });
            result += "</tr></thead>";

            result += "<tbody>";
            $(gridData).each(function (key, value) {
                result += "<tr>";
                $($settings.columns).each(function (k, v) {
                    if (value.hasOwnProperty(this.datafield)) {
                        if (this.ishidden != true) {
                            result += "<td";
                            if (this.width != null) {
                                result += " style='width: " + this.width + "'";
                            }
                            result += ">";
                            if(isNaN(value[this.datafield]))
                            {
                            	result += value[this.datafield]; 
                            }
                            else
                            {
                            	if(value[this.datafield].length > 9)
                            	{
                            		
                            		
                            		var i = "";
                            		for(var j=0;j<=(value[this.datafield].length-1) ;j++)
                            		{
                            			i +="0";
                            		}
                            		result += '=TEXT('+value[this.datafield]+',&quot;'+i+'&quot;)';
                            	}
                            	else
                            	{
                            		result += value[this.datafield];
                            	}
                            	
                        		
                        		
                        		/*if(regExp.test(value[this.datafield]))
                            	{
                            		var i = null;
                            		for(var i in value[this.datafield])
                            		{
                            			i +="0";
                            		}
                            		result += '=TEXT('+value[this.datafield]+',&quot;'+i+'&quot;)';
                            	}
                            	else
                            	{
                            		result += '=TEXT('+value[this.datafield]+',&quot;0&quot;)';
                            	}*/
                            	
                            	//result += "&#39;"+value[this.datafield];
                            }
                            
                            result += "</td>";
                        }
                    }
                });
                result += "</tr>";
            });
            result += "</tbody>";

            result += "</table>";
            return result;
        }

        function Export(htmltable) {
            var excelFile = "<html xmlns:o='urn:schemas-microsoft-com:office:office' xmlns:x='urn:schemas-microsoft-com:office:excel' xmlns='http://www.w3.org/TR/REC-html40'>";
            excelFile += "<head>";
            excelFile += '<meta http-equiv="Content-type" content="text/html;charset=' + $defaults.encoding + '" />';
            excelFile += "<!--[if gte mso 9]>";
            excelFile += "<xml>";
            excelFile += "<x:ExcelWorkbook>";
            excelFile += "<x:ExcelWorksheets>";
            excelFile += "<x:ExcelWorksheet>";
            excelFile += "<x:Name>";
            excelFile += "{worksheet}";
            excelFile += "</x:Name>";
            excelFile += "<x:WorksheetOptions>";
            excelFile += "<x:DisplayGridlines/>";
            excelFile += "</x:WorksheetOptions>";
            excelFile += "</x:ExcelWorksheet>";
            excelFile += "</x:ExcelWorksheets>";
            excelFile += "</x:ExcelWorkbook>";
            excelFile += "</xml>";
            excelFile += "<![endif]-->";
            excelFile += "</head>";
            excelFile += "<body>";
			excelFile += reportHead;
            excelFile += htmltable.replace(/"/g, '\'');
            excelFile += "</body>";
            excelFile += "</html>";

            var uri = "data:application/vnd.ms-excel;base64,";
            var ctx = { worksheet: $settings.worksheetName, table: htmltable };

            //return (uri + base64(format(excelFile, ctx)));
			return format(excelFile, ctx);
        }

        function base64(s) {
            return window.btoa(unescape(encodeURIComponent(s)));
        }

        function format(s, c) {
            return s.replace(/{(\w+)}/g, function (m, p) { return c[p]; });
        }
    };
})(jQuery);
