document.addEventListener("DOMContentLoaded", function() {
    console.log("hello world");

     window.onscroll = () => {
         if (window.innerHeight + window.scrollY >= document.documentElement.scrollHeight) {
             // Simulate fetching more content (replace this with actual data fetching)
             fetchMoreContent();
         }
     };

     function fetchMoreContent() {

       

         // Simulated delay to mimic data fetching
         setTimeout(() => {
             // Create new paragraph elements with some dummy content
             for (let i = 0; i < 5; i++) {
                 const p = document.createElement('post');
                 p.
                 p.textContent = "This is a new paragraph";
                 p.style.color = "red";
                 document.body.appendChild(p);
             }
         }, 1000); // Adjust delay as needed
    }
});
