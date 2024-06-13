document.addEventListener('DOMContentLoaded', function() {
    const fileInput = document.getElementById('fileInput');
    const uploadButton = document.getElementById('uploadButton');
    const status = document.getElementById('status');

    uploadButton.addEventListener('click', function() {
        const file = fileInput.files[0];

        if (!file) {
            status.textContent = 'Please select a file first.';
            return;
        }

        const formData = new FormData();
        formData.append('file', file);

        fetch('/upload', {
            method: 'POST',
            body: formData
        })
            .then(response => {
                if (response.ok) {
                    return response.text();
                } else {
                    throw new Error('File upload failed.');
                }
            })
            .then(data => {
                status.textContent = 'File uploaded successfully.';
            })
            .catch(error => {
                status.textContent = error.message;
            });
    });
});
