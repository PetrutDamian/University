function f = cuv(email)
fileID = fopen('spam.txt','r');
spam = textscan(fileID,'%s');
fclose(fileID);
%fprintf(" %s",spam{1}{:});

fileID2 = fopen('ham.txt','r');
ham = textscan(fileID2,'%s');
fclose(fileID2);

fileID3 = fopen(email,'r');
email = textscan(fileID3,'%s');
fclose(fileID3);

words = unique([spam{1};ham{1}]);
probspam = numel(spam{1})/numel(spam{1})+numel(ham{1});
probham =  numel(ham{1})/numel(ham{1})+numel(spam{1});

for i=1:numel(words)
    sumspam = sum(strcmp(words{i},spam{1}))/numel(spam{1});
    sumham = sum(strcmp(words{i},ham{1}))/numel(ham{1});
    if ismember(words{i}, email{1})
        probspam = probspam*sumspam;
        probham = probham * sumham;
    else
        probspam = probspam*(1-sumspam);
        probham = probham * (1-sumham);
    end
end
if probspam>probham
    disp('Email-ul este spam!');
else
    disp('Email-ul este ham!');
end
end