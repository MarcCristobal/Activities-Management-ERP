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

    function addListItem(listId, inputId) {
        var input = document.getElementById(inputId);
        var list = document.getElementById(listId);

        // Obtén el valor del campo de entrada
        var text = input.value;

        // Crea un nuevo elemento de lista y asigna el texto
        var newListItem = document.createElement('li');
        newListItem.textContent = text;

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
        
        console.log("ha pasado");
    }

    function updateHiddenField(listId) {
        console.log("ha pasado");
        
        var list = document.getElementById(listId);
        var resources = [];

        // Recorre los elementos de la lista y agrega los textos al array
        for (var i = 0; i < list.children.length; i++) {
            resources.push(list.children[i].textContent);
        }

        // Imprime en la consola para verificar
        console.log(listId + 'Hidden:', JSON.stringify(resources));

        // Actualiza el valor del campo oculto con el array de recursos
        document.getElementById(listId + 'Hidden').value = JSON.stringify(resources);
    }
});
