document.addEventListener('DOMContentLoaded', () => {


    document.querySelectorAll('.card-header').forEach(header => {
        header.addEventListener('click', () => {
            header.parentElement.classList.toggle('is-expanded');
        });
    });


    const createJobForm = document.querySelector('form[action="/api/jobs"]');
    if (createJobForm) {
        createJobForm.addEventListener('submit', function (e) {
            e.preventDefault();
            fetch(this.action, {
                method: 'post',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    jobTitle: document.getElementById('jobTitle').value,
                    content: document.getElementById('jobContent').value
                })
            }).then(response => {
                if (response.ok) window.location.reload();
                else alert('Failed to create job.');
            });
        });
    }

    const screenForm = document.getElementById('screenForm');
    if (screenForm) {
        screenForm.addEventListener('submit', function (e) {
            e.preventDefault();
            const analyzeBtn = this.querySelector('button[type="submit"]');
            const loader = analyzeBtn.querySelector('.loader');

            // Show loader and disable button
            loader.style.display = 'inline-block';
            analyzeBtn.disabled = true;
            analyzeBtn.querySelector('.btn-text').textContent = 'Analyzing...';

            const selectedJobId = document.getElementById('jobSelect').value;
            fetch(this.action, {
                method: 'post',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    jobId: selectedJobId,
                    resumeId: document.getElementById('resumeSelect').value
                })
            }).then(response => {
                if (response.ok) window.location.href = '/?jobId=' + selectedJobId;
                else alert('Failed to run analysis.');
            }).finally(() => {
                // --- Hide loader and re-enable button (in case of error) ---
                loader.style.display = 'none';
                analyzeBtn.disabled = false;
                analyzeBtn.querySelector('.btn-text').textContent = 'Analyze';
            });
        });
    }


    document.querySelectorAll('.delete-job-btn').forEach(button => {
        button.addEventListener('click', function(e) {
            e.preventDefault();
            const jobId = this.getAttribute('data-id');
            if (confirm('Are you sure you want to delete this job? This action cannot be undone.')) {
                fetch(`/api/jobs/${jobId}`, { method: 'DELETE' })
                    .then(response => {
                        if(response.ok) window.location.reload();
                        else alert('Failed to delete job.');
                    });
            }
        });
    });

    document.querySelectorAll('.delete-resume-btn').forEach(button => {
        button.addEventListener('click', function(e) {
            e.preventDefault();
            const resumeId = this.getAttribute('data-id');
            if (confirm('Are you sure you want to delete this resume? This action cannot be undone.')) {
                fetch(`/api/resumes/${resumeId}`, { method: 'DELETE' })
                    .then(response => {
                        if(response.ok) window.location.reload();
                        else alert('Failed to delete resume.');
                    });
            }
        });
    });

    // New MODAL LOGIC
    const modalOverlay = document.getElementById('modalOverlay');
    const modalContent = document.getElementById('modalContent');
    const modalCloseBtn = document.getElementById('modalCloseBtn');

    document.querySelectorAll('.view-details-btn').forEach(button => {
        button.addEventListener('click', function() {
            const resultCard = this.closest('.result-item');
            const candidate = resultCard.querySelector('.result-candidate').textContent;
            const score = resultCard.querySelector('.result-score').textContent;
            const skills = resultCard.querySelector('.result-skills').textContent;
            const justification = resultCard.querySelector('.result-justification').textContent;

            modalContent.innerHTML = `
                <button id="modalCloseBtn" class="modal-close-btn">&times;</button>
                <h2>${candidate}</h2>
                <p><strong>Score:</strong> ${score}</p>
                <p><strong>Extracted Skills:</strong> ${skills}</p>
                <p><strong>Justification:</strong> ${justification}</p>
            `;
            modalOverlay.style.display = 'flex';

            document.getElementById('modalCloseBtn').addEventListener('click', () => {
                modalOverlay.style.display = 'none';
            });
        });
    });

    // Close modal if user clicks on the overlay background
    if(modalOverlay) {
        modalOverlay.addEventListener('click', function(e) {
            if (e.target === modalOverlay) {
                modalOverlay.style.display = 'none';
            }
        });
    }
});