<!DOCTYPE html>
<html>
<head>
    <title>Expense Approval Request</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            color: #333;
            margin: 0;
            padding: 0;
        }

        .container {
            max-width: 600px;
            margin: 0 auto;
            padding: 20px;
            background-color: #f5f5f5;
            border-radius: 5px;
        }

        h1 {
            color: #333;
            font-size: 24px;
            margin-bottom: 20px;
        }

        p {
            font-size: 16px;
            line-height: 24px;
            margin-bottom: 20px;
        }

        table {
            border-collapse: collapse;
            width: 100%;
        }

        th, td {
            padding: 10px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }

        th {
            background-color: #f2f2f2;
            font-weight: bold;
        }

        .button {
            color: #fffff;
            display: inline-block;
            padding: 10px 20px;
            text-decoration: none;
            border-radius: 4px;
            font-weight: bold;
            margin-right: 10px;
        }

        .button.approve {
            background-color: #088F8F;
            color:white;
        }

        .button.reject {
            background-color: #8c190d;
             color:white;
        }

        .button:hover {
            opacity: 0.8;
        }

        .signature {
            margin-top: 45px;
            font-size: 16px;
        }

        .signature p {
            margin: 5px 0;
        }

        .support {
            font-size: 16px;
            margin-top: 20px;
        }

        .support a {
            color: #316fea;
            text-decoration: none;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Expense Approval Request</h1>
        <p>
            Dear Sir,
            <br>
            I hope this message finds you well.<br>
            I am requesting your approval for an expense request that requires your review.Below are the details :
        </p>
        <table>
           <tr>
             <th>Employee Name</th>
             <td>${EmployeeName!''}</span></td>
           </tr>
            <tr>
                <th>Expense Amount</th>
                <td>${ExpenseAmount!''}</td>
            </tr>
            <tr>
                <th>Expense Date</th>
                <td>${ExpenseDate!''}</td>
            </tr>
            <tr>
                <th>Expense Purpose</th>
                <td>${ExpensePurpose!''}</td>
            </tr>
        </table>
        <p> I would greatly appreciate your review and decision on this request. Kindly indicate your choice by selecting one of the options below:</p>


        <a class="button approve" href="${approvalUrl}" target="_blank" rel="noopener">Approve</a>
        <a class="button reject" href="${rejectionUrl}" target="_blank" rel="noopener">Reject</a>
        <div class="support">
            <p>
                Please let me know if you require any additional information or documentation.
            </p><br>
            <p>Thank you for your attention to this matter.</p>
        </div>
        <div class="signature">
            <p>Best Regards,</p>
            <p>Alphadot Technologies Team</p>
        </div>
    </div>
</body>
</html>
