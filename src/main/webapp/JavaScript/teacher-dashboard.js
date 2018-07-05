function onSubmit() {
    var features = [];
    $('.feature input[type="checkbox"]:checked').each(function() {
        features.push($(this).val());
    });
    $('#selectedFeatures').html(features.join(','));
}