async function uploadChunk(chunk, fileName, chunkIndex) {
    const formData = new FormData();
    formData.append('file', chunk);
    formData.append('fileName', fileName);
    formData.append('chunkIndex', chunkIndex);

    try {
        await fetch('/uploadChunk', {
            method: 'POST',
            body: formData
        });
        console.log(`Chunk ${chunkIndex} uploaded successfully.`);
    } catch (error) {
        console.error(`Error uploading chunk ${chunkIndex}:`, error);
    }
}

async function completeUpload(fileName, totalChunks) {
    try {
        await fetch('/completeUpload', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                fileName: fileName,
                totalChunks: totalChunks
            })
        });
        console.log('File upload completed successfully.');
    } catch (error) {
        console.error('Error completing upload:', error);
    }
}

async function uploadFile(file) {
    const CHUNK_SIZE = 100 * 1024 * 1024; // 100MB
    const totalChunks = Math.ceil(file.size / CHUNK_SIZE);

    const chunkPromises = [];

    for (let chunkIndex = 0; chunkIndex < totalChunks; chunkIndex++) {
        const start = chunkIndex * CHUNK_SIZE;
        const end = Math.min(start + CHUNK_SIZE, file.size);
        const chunk = file.slice(start, end);

        chunkPromises.push(uploadChunk(chunk, file.name, chunkIndex));
    }

    await Promise.all(chunkPromises);

    await completeUpload(file.name, totalChunks);
}

document.getElementById('fileInput').addEventListener('change', (event) => {
    const file = event.target.files[0];
    if (file) {
        uploadFile(file);
    }
});
