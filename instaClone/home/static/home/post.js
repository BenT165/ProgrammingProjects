function uploadImage() {
  var input = document.createElement('input');
  input.type = 'file';
  input.accept = 'image/jpeg, image/png';
  input.click();
  
  input.addEventListener('change', function() {
      var selectedFile = input.files[0];
      var reader = new FileReader();
      
      reader.onload = function(event) {
          var img = new Image();
          img.onload = function() {
              var frame = document.getElementById('frame');
              var widthRatio = frame.offsetWidth / img.width;
              var heightRatio = frame.offsetHeight / img.height;
              var ratio = Math.max(widthRatio, heightRatio);
              var width = img.width * ratio;
              var height = img.height * ratio;

              img.style.width = width + 'px';
              img.style.height = height + 'px';

              frame.innerHTML = ''; // Clear previous content
              frame.appendChild(img);
          };

          img.src = event.target.result;
      };
      
      reader.readAsDataURL(selectedFile);

      // Render a square text box that user can type text into
      var textbox = document.getElementById("textbox");
      textbox.style.display = "flex";
      
      var caption = document.getElementById("caption"); // Correctly reference the caption element

      textbox.addEventListener("click", function() {
          // Ensure the element exists and has a display style set to "flex"
          if (this.style.display !== "flex") {
              return;
          }
          
          // Assuming you want to clear the content of an element with id "caption"
          if (caption) {
              console.log("hello");
              caption.style.color = "black";
              caption.style.border = 'none';
              caption.style.outline = 'none';
              caption.contentEditable = "true";
              
          }
      });
  });
}
