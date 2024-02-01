(function () {
    function previewImage(input) {
        var preview = document.getElementById('previewImage');
        if (input.files && input.files[0]) {
            var reader = new FileReader();
            reader.onload = function (e) {
                preview.src = e.target.result;
                preview.style.display = 'block'; // Mostrar la imagen previa cuando se carga
            };
            reader.readAsDataURL(input.files[0]);
        }
    }

    // Función para agregar un retraso antes de redirigir
    function delayRedirect() {
        // Verificar si la imagen se ha cargado completamente
        var image = new Image();
        image.onload = function () {
            // Redirigir después de completar la carga de la imagen
            window.location.href = "/home/customers";
        };
        image.src = document.getElementById('previewImage').src;
    }

    // Adjuntar la función al evento onchange del input
    var photoInput = document.getElementById('photo');
    if (photoInput) {
        photoInput.addEventListener('change', function () {
            previewImage(this);
        });
    }

    // Llama a la función cuando el formulario se envía con éxito
    var form = document.querySelector('form');
    if (form) {
        form.addEventListener('submit', function (event) {
            event.preventDefault(); // Evitar que el formulario se envíe automáticamente
            delayRedirect(); // Llamar a la función de retraso
        });
    }

    // Asegurar que la imagen se ha cargado completamente antes de mostrarla
    document.addEventListener('DOMContentLoaded', function () {
        var preview = document.getElementById('previewImage');
        preview.style.display = 'block';
    });
})();
