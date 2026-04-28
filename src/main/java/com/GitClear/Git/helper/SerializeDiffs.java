package com.GitClear.Git.helper;

import com.GitClear.Git.model.DiffLine;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SerializeDiffs {
    public String serialize(List<DiffLine> diffLineList)
    {
        StringBuilder sb=new StringBuilder();
        for(DiffLine diffLine:diffLineList)
        {
            sb.append("DiffContent: ").
                    append(diffLine.getDiffContent()).
                    append("\n").append("DiffType: ").
                    append(diffLine.getType()).append("\n");
        }
        return sb.toString();
    }
}
