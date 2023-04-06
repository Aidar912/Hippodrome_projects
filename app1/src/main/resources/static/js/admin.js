
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
});

function updateHorsesSelect() {
    var raceId = $('#race').val();
    $.ajax({
        url: '/',
        type: 'POST',
        data: { raceId: raceId },
        success: function(horses) {
            var options = '';
            horses.forEach(function(horse) {
                options += '<option value="' + horse.id + '">' + horse.name + '</option>';
            });
            $('#horse').html(options);
        }
    });
}