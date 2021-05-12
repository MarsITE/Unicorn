<!DOCTYPE html>
<html>
<head>
 <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>

<body>
    <div align="left">
        <h3>Hello, ${email}!</h3>
        <h4>You added new skills</h4>
        <#if isPresentApprovedSkills>
            <p>New approved skills:</p>
            <ul>
                <#list approvedSkills as skill>
                    <li>${skill}</li>
                </#list>
            </ul>
        </#if>
        <#if isPresentUnapprovedSkills>
            <p>New skills that are waiting for approve by admin:</p>
            <ul>
                <#list unapprovedSkills as skill>
                    <li>${skill}</li>
                </#list>
            </ul>
        </#if>
    </div>
    <div align="left">
        <p><b>Administrator</b></p>
        <p>WorkSearch</p>
    </div>
</body>
<html>