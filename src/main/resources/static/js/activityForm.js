document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('addResource').addEventListener('click', function () {
        addListItem('resourceList', 'resource');
    });

    document.getElementById('addRequirement').addEventListener('click', function () {
        addListItem('requirementList', 'requirement');
    });

    document.getElementById('submit').addEventListener('click', function () {
        updateHiddenField('resourceList');
        updateHiddenField('requirementList');
    });

    document.getElementById("isFree").addEventListener("change", function () {
        // Cuando se hace clic en el checkbox "Yes"
        togglePaymentOptions(this.checked);
    });

    document.getElementById("isNotFree").addEventListener("change", function () {
        // Cuando se hace clic en el checkbox "No"
        togglePaymentOptions(!this.checked);
    });

    document.getElementById("limited").addEventListener("change", function () {
        // Cuando se hace clic en el radio button "Limited"
        toggleParticipantsSection();
    });

    // Adjunta event listeners para la lista de recursos y la lista de requisitos
    attachDeleteListeners('resourceList');
    attachDeleteListeners('requirementList');

    toggleParticipantsSection();
    togglePaymentOptions();

    applyBackground('resourceList');
    applyBackground('requirementList');

    function addListItem(listId, inputId) {
        var input = document.getElementById(inputId);
        var list = document.getElementById(listId);

        // Obtén el valor del campo de entrada
        var text = input.value;

        // Crea un nuevo elemento de lista y asigna el texto
        var newListItem = document.createElement('li');
        newListItem.className = "flex items-center justify-between group"; // Aplica las clases necesarias al <li>

        // Crea un span para el texto del elemento de lista
        var spanText = document.createElement('span');
        spanText.textContent = text;

        // Crea una imagen para el ícono de eliminar
        var trashIcon = document.createElement('img');
        trashIcon.src = "/images/contenedor-de-basura2.png";
        trashIcon.className = "size-4 invisible transition duration-300 ease-in-out group-hover:visible"; // Aplica las clases necesarias al ícono de eliminar

        // Adjunta un event listener a la imagen de basura recién creada
        trashIcon.addEventListener('click', function () {
            deleteListItem(trashIcon); // Llama a la función deleteListItem cuando se hace clic en la imagen
        });

        // Agrega el texto y el ícono de eliminar al nuevo elemento de lista
        newListItem.appendChild(spanText);
        newListItem.appendChild(trashIcon);

        // Obtén la cantidad de elementos en la lista antes de agregar el nuevo elemento
        var index = list.children.length;

        // Aplica clase de Tailwind condicional basada en el índice (solo en el caso de índice impar)
        if (index % 2 !== 0) {
            newListItem.classList.add('bg-gray-200'); // Puedes ajustar el color según tus necesidades
        }

        // Agrega el nuevo elemento a la lista
        list.appendChild(newListItem);

        // Borra el contenido del campo de entrada
        input.value = '';

        // Luego de agregar el elemento, aplicar el fondo correspondiente
        applyBackground(listId);
    }

    function updateHiddenField(listId) {
        var list = document.getElementById(listId);
        var resources = [];

        // Recorre los elementos de la lista y agrega los textos al array
        for (var i = 0; i < list.children.length; i++) {
            resources.push(list.children[i].textContent);
        }

        // Actualiza el valor del campo oculto con el array de recursos
        document.getElementById(listId + 'Hidden').value = JSON.stringify(resources);
    }

    function toggleParticipantsSection() {
        var limitedCheckbox = document.getElementById("limited");
        var participantsSection = document.getElementById("participantsSection");

        if (limitedCheckbox.checked) {
            participantsSection.classList.remove('invisible');
            validateParticipantLimit();
        } else {
            participantsSection.classList.add('invisible');
        }
    }

    function togglePaymentOptions(isFree) {
        var pricePerPersonContainer = document.getElementById("pricePerPersonContainer");
        var numberOfPaymentsContainer = document.getElementById("numberOfPaymentsContainer");

        if (isFree) {
            pricePerPersonContainer.classList.remove('invisible');
            numberOfPaymentsContainer.classList.remove('invisible');
            validatePaymentValues();
        } else {
            pricePerPersonContainer.classList.add('invisible');
            numberOfPaymentsContainer.classList.add('invisible');
        }
    }

    // Función para agregar event listeners a las imágenes dentro de una lista dada
    function attachDeleteListeners(listId) {
        // Obtén todas las imágenes dentro de la lista con el ID dado
        var listImages = document.querySelectorAll('#' + listId + ' img');

        // Itera sobre cada imagen y asigna un event listener
        listImages.forEach(function (image) {
            image.addEventListener('click', function () {
                deleteListItem(this); // Llama a la función deleteListItem cuando se hace clic en la imagen
            });
        });
    }

    function deleteListItem(image) {
        var listItem = image.parentNode; // Obtén el elemento <li> que contiene la imagen
        var listId = listItem.parentNode.id; // Obtén el ID de la lista a partir del elemento <ul>

        // Elimina el elemento <li> de la lista en el DOM
        listItem.parentNode.removeChild(listItem);

        // Después de eliminar el elemento, aplicar el fondo correspondiente
        applyBackground(listId);
    }

    function applyBackground(listId) {
        var list = document.getElementById(listId);
        var listItems = list.children;

        for (var i = 0; i < listItems.length; i++) {
            if (i % 2 !== 0) {
                listItems[i].classList.add('bg-gray-200');
            } else {
                listItems[i].classList.remove('bg-gray-200');
            }
        }
    }

});
