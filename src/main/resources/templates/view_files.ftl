<script src="/js/vue.js"></script>
<script src="/js/axios.min.js"></script>
<script src="/js/pdfjs-2.5.207-dist/build/pdf.js"></script>
<script src="/js/mammoth.browser.js"></script>
<script>
  document.addEventListener('DOMContentLoaded', function(){
     let filePath=document.getElementById('file-input').value;

     if(filePath.includes('.docx')){
      axios.get(filePath, {
           responseType: "arraybuffer",
         }).then(response=>{
         mammoth.convertToHtml({arrayBuffer: response.data}).then(function (resultObject) {
                                     document.getElementById('the-frame').innerHTML = resultObject.value
                                   });
             /*var reader = new FileReader();
             var file = new File(response.data);

              reader.onloadend = function(event) {
                          var arrayBuffer = reader.result;

                          mammoth.convertToHtml({arrayBuffer: arrayBuffer}).then(function (resultObject) {
                            document.getElementById('the-frame').innerHTML = resultObject.value
                          });
              };

              reader.readAsArrayBuffer(file);*/
         });
     } else {
         var loadingTask = pdfjsLib.getDocument(filePath);
         loadingTask.promise.then(function(pdf) {
         pdf.getPage(1).then(function(page) {
           var scale = 1.5;
           var viewport = page.getViewport({ scale: scale, });

           var canvas = document.getElementById('the-canvas');
           var context = canvas.getContext('2d');
           canvas.height = viewport.height;
           canvas.width = viewport.width;

           var renderContext = {
             canvasContext: context,
             viewport: viewport
           };
           page.render(renderContext);
           })
         });
     }
  });
</script>
<input type="hidden" value="${fileSrc}" id="file-input"/>
<canvas id="the-canvas"></canvas>
<div id="the-frame"></div>