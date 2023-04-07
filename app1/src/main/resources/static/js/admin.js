
const links = document.querySelectorAll('.select_a');
const blocks = document.querySelectorAll('.select_block');

links.forEach((link) => {
    link.addEventListener('click', (event) => {
        event.preventDefault();
        const targetId = link.getAttribute('href').substring(1);
        const targetBlock = document.getElementById(targetId);
        blocks.forEach((block) => {
            block.classList.add('hidden');
            block.classList.remove('show_blocks')
        });
        targetBlock.classList.remove('hidden');
        targetBlock.classList.add('show_blocks');
    });
});

$(document).ready(function() {
    var message = getParameterByName('message');
    if (message) {
        $('#alert-message').text("Недостаточно средств! " +
            "Пополните баланс!");
        $('#myModal').modal('show');
        window.history.replaceState(null, null, window.location.pathname);
        clearMessageParam();
    }
});

function getParameterByName(name, url) {
    if (!url) url = window.location.href;
    name = name.replace(/[\[\]]/g, '\\$&');
    var regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)'),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, ' '));
}

function clearMessageParam() {
    var newUrl = window.location.href.replace(/(\?|&)*message=[^&]+(&)*/gi, '$1').replace(/(\?|&)+$/gi, '');
    window.history.replaceState({}, document.title, newUrl);
}
blocks.forEach((block) => {
    block.classList.add('hidden');
});
$(document).ready(function() {
    // При загрузке страницы заполнить второй select для выбранного значения в первом select
    updateHorsesSelect();

    // При изменении первого select заполнить второй select для выбранного значения
    $('#race').on('change', function() {
        updateHorsesSelect();
    });

    // При изменении типа ставки
    $('#betType').on('change', function() {
        updateHorsesSelect();
    });
});
function updateHorsesSelect() {
    var raceId = $('#race').val();
    var betType = $('#betType').val();
    // Вызываем нужную функцию calculateCoefficient()
    var calculateCoefficientFunction = function(id) { return ''; }; // Задаем функцию по умолчанию
    if (betType === 'WINNER') {
        calculateCoefficientFunction = calculateCoefficientWin;
    } else if (betType === 'LAST') {
        calculateCoefficientFunction = calculateCoefficientLose;
    }

    // Запрашиваем лошадей и их коэффициенты
    $.ajax({
        url: '/horses',
        type: 'POST',
        data: { raceId: raceId },
        success: function(horses) {
            var options = '';
            horses.forEach(function(horse) {
                options += '<option value="' + horse.id + '">' + horse.name + ' - кф ' + calculateCoefficientFunction(horse.id) + '</option>';
            });
            $('#horse').html(options);
            $('#coefficient').val(calculateCoefficientFunction($('#horse').val())); // Устанавливаем начальное значение для #coefficient
        }
    });

    // Обработчик события change для #horse
    $('#horse').on('change', function() {
        $('#coefficient').val(calculateCoefficientFunction($(this).val())); // Обновляем значение #coefficient при выборе новой лошади
    });
}

function calculateCoefficientWin(id) {
    // Вызываем функцию calculateCoefficientWin()
    var coefficient;
    $.ajax({
        url: '/coefficient/win',
        type: 'POST',
        data: { horseId: id },
        async: false,
        success: function(data) {
            coefficient = data;
        }
    });

    return coefficient;
}

function calculateCoefficientLose(id) {
    // Вызываем функцию calculateCoefficientLose()
    var coefficient;
    $.ajax({
        url: '/coefficient/lose',
        type: 'POST',
        data: { horseId: id },
        async: false,
        success: function(data) {
            coefficient = data;
        }
    });

    return coefficient;
}