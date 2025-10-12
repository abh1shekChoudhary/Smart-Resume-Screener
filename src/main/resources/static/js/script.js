document.addEventListener('DOMContentLoaded', () => {

    // Interactive Card Functionality
    const cardHeaders = document.querySelectorAll('.card-header');
    cardHeaders.forEach(header => {
        header.addEventListener('click', () => {
            const card = header.parentElement;
            card.classList.toggle('is-expanded');
        });
    });

    // Form Submission Logic
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
            });
        });
    }


    // Handle deleting jobs
    document.querySelectorAll('.delete-job-btn').forEach(button => {
        button.addEventListener('click', function(e) {
            e.preventDefault();
            const jobId = this.getAttribute('data-id');
            if (confirm('Are you sure you want to delete this job? This action cannot be undone.')) {
                fetch(`/api/jobs/${jobId}`, {
                    method: 'DELETE'
                }).then(response => {
                    if(response.ok) window.location.reload();
                    else alert('Failed to delete job.');
                });
            }
        });
    });

    // Handle deleting resumes
    document.querySelectorAll('.delete-resume-btn').forEach(button => {
        button.addEventListener('click', function(e) {
            e.preventDefault();
            const resumeId = this.getAttribute('data-id');
            if (confirm('Are you sure you want to delete this resume? This action cannot be undone.')) {
                fetch(`/api/resumes/${resumeId}`, {
                    method: 'DELETE'
                }).then(response => {
                    if(response.ok) window.location.reload();
                    else alert('Failed to delete resume.');
                });
            }
        });
    });
});