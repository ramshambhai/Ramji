
var tablesToExcel = (function() {
			
			
    var uri = 'data:application/vnd.ms-excel;base64,'
    , tmplWorkbookXML = '<?xml version="1.0"?><?mso-application progid="Excel.Sheet"?><Workbook xmlns="urn:schemas-microsoft-com:office:spreadsheet" xmlns:ss="urn:schemas-microsoft-com:office:spreadsheet">'
      + '<DocumentProperties xmlns="urn:schemas-microsoft-com:office:office"><Author>Axel Richter</Author><Created>{created}</Created></DocumentProperties>'
      + '<Styles>'
      + '<Style ss:ID="Currency"><NumberFormat ss:Format="Currency"></NumberFormat></Style>'
      + '<Style ss:ID="Date"><NumberFormat ss:Format="Medium Date"></NumberFormat></Style>'
      + '</Styles>' 
      + '{worksheets}</Workbook>'
    , tmplWorksheetXML = '<Worksheet ss:Name="{nameWS}"><Table>{rows}</Table></Worksheet>'
    , tmplCellXML = '<Cell{attributeStyleID}{attributeFormula}><Data ss:Type="{nameType}">{data}</Data></Cell>'
    , base64 = function(s) { return window.btoa(unescape(encodeURIComponent(s))) }
    , format = function(s, c) { return s.replace(/{(\w+)}/g, function(m, p) { return c[p]; }) }
    return function(columnArray,arr,wsnames, wbname, appname) {
      var ctx = "";
      var workbookXML = "";
      var worksheetsXML = "";
      var rowsXML = "";
      var columns=[];

      for (var i = 0; i < arr.length; i++) {
	  rowsXML += '<Row>';
	  columns=Object.keys(columnArray[i]);
	  
	  	for(var count=0;count<columns.length;count++)
	  	{
		              ctx = { attributeStyleID: ''
                   , nameType: 'String'
                   , data: columns[count]
                   , attributeFormula: ''
                  };
				  rowsXML += format(tmplCellXML, ctx);
	  }
	            rowsXML += '</Row>';
				
		rowsXML += '<Row>';
		
	  	for(var space=0;space<columns.length;space++){
		              ctx = { attributeStyleID: ''
                   , nameType: 'String'
                   , data: ''
                   , attributeFormula: ''
                  };
				  rowsXML += format(tmplCellXML, ctx);
	  }
	            rowsXML += '</Row>';
				
				
        //if (!tables[i].nodeType) tables[i] = document.getElementById(tables[i]);
        for (var j = 0; j < arr[i].length; j++) 
        {
          rowsXML += '<Row>';
		  var al=arr[i][j];
		  
		  for(var zz in columnArray[i])
		  {
			  dataValue = al[columnArray[i][zz]];
			  ctx = { attributeStyleID: ''
                  , nameType: 'String'
                  , data: dataValue
                  , attributeFormula: ''
                 };
           rowsXML += format(tmplCellXML, ctx);
			  
		  }
		  
          /*for (var k = 0; k < Object.keys(al).length; k++) 
          {
		  //alert(al[Object.keys(al)[k]]);
            dataValue = al[Object.keys(al)[k]];

            ctx = { attributeStyleID: ''
                   , nameType: 'String'
                   , data: dataValue
                   , attributeFormula: ''
                  };
            rowsXML += format(tmplCellXML, ctx);
          }*/
          rowsXML += '</Row>';
        }
        ctx = {rows: rowsXML, nameWS: wsnames[i] || 'Sheet' + i};
        worksheetsXML += format(tmplWorksheetXML, ctx);
        rowsXML = "";
      }
      ctx = {created: (new Date()).getTime(), worksheets: worksheetsXML};
      workbookXML = format(tmplWorkbookXML, ctx);


	  var blob=new Blob([format(tmplWorkbookXML, ctx)]);
      var link = document.createElement("A");
      //link.href = uri + base64(workbookXML);
	  //link.href=format(excelFile, ctx);
	  link.href=window.URL.createObjectURL(blob);
      link.download = wbname || 'Workbook.xls';
      link.target = '_blank';
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);

    }
  })();


