package org.sonarsource.plugins.example.scm;


import com.alibaba.fastjson.JSON;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.util.io.DisabledOutputStream;
import org.sonar.api.internal.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class CommitUtil {

    public static String establishCommitHistory(String gitPath) throws IOException, GitAPIException {
        // PART 0. preparation
        if (gitPath.contains("\\")) {
            gitPath = gitPath.replaceAll("\\\\", "/");
        }

        Repository repository = null;
        try {
            repository = new FileRepositoryBuilder()
                    .findGitDir(new File(gitPath))
                    .build();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return "[]";
        }
        Git git = new Git(repository);

        // PART 1. iterates over commits on all branches
        List<SimpleCommit> simpleCommitList = new ArrayList<>();

        Iterable<RevCommit> commits = git.log().all().call();
        int count = 0;
        int sha1Start = 7;
        int sha1End = 15;
        for (RevCommit commit : commits) {
            if (commit.getParentCount() == 1) {
                DiffResult diffResult = diffCommits(git, commit, commit.getParent(0));
                SimpleCommit sc = new SimpleCommit(commit.getId().toString().substring(sha1Start, sha1End), commit.getCommitTime(), diffResult.linesAdded, diffResult.lineDeleted);
                simpleCommitList.add(sc);
            }
            count++;
        }
        return JSON.toJSONString(simpleCommitList);
    }

    private static Repository findRepo(String path) throws IOException {
        Repository repository = new FileRepositoryBuilder()
                .findGitDir(new File(path))
                .build();
        return repository;
    }

    public static class SimpleCommit implements Serializable {
        String id;
        int ts;
        int la;
        int ld;
//        int filesChanged;

        public SimpleCommit(String id, int timestamp, int linesAdded, int lineDeleted) {
            this.id = id;
            this.ts = timestamp;
            this.la = linesAdded;
            this.ld = lineDeleted;
//            this.filesChanged = filesChanged;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getTs() {
            return ts;
        }

        public void setTs(int ts) {
            this.ts = ts;
        }

        public int getLa() {
            return la;
        }

        public void setLa(int la) {
            this.la = la;
        }

        public int getLd() {
            return ld;
        }

        public void setLd(int ld) {
            this.ld = ld;
        }
    }

    private static class DiffResult {
        int linesAdded;
        int lineDeleted;
        int filesChanged;

        public DiffResult(int linesAdded, int lineDeleted, int filesChanged) {
            this.linesAdded = linesAdded;
            this.lineDeleted = lineDeleted;
            this.filesChanged = filesChanged;
        }
    }

    private static DiffResult diffCommits(Git git, RevCommit newCommit, RevCommit oldCommit) throws IOException {
//        // Obtain tree iterators to traverse the tree of the old/new commit
//        ObjectReader reader = git.getRepository().newObjectReader();
//        CanonicalTreeParser oldTreeIter = new CanonicalTreeParser();
//        oldTreeIter.reset(reader, oldCommit.getTree());
//        CanonicalTreeParser newTreeIter = new CanonicalTreeParser();
//        newTreeIter.reset(reader, newCommit.getTree());
//
//        // Use a DiffFormatter to compare new and old tree and return a list of changes
//        DiffFormatter diffFormatter = new DiffFormatter(DisabledOutputStream.INSTANCE);
//        diffFormatter.setRepository(git.getRepository());
//        diffFormatter.setContext(0);
//        List<DiffEntry> entries = diffFormatter.scan(oldTreeIter, newTreeIter);
//
//        // Print the contents of the DiffEntries
//        for (DiffEntry entry : entries) {
//            System.out.println(entry);
//            FileHeader fileHeader = diffFormatter.toFileHeader(entry);
//            List<? extends HunkHeader> hunks = fileHeader.getHunks();
//            for (HunkHeader hunk : hunks) {
//                System.out.println(hunk);
//            }
//        }


        //APPROACH 2:
        int linesAdded = 0;
        int linesDeleted = 0;
        int filesChanged = 0;

        DiffFormatter df = new DiffFormatter(DisabledOutputStream.INSTANCE);
        df.setRepository(git.getRepository());
        df.setDiffComparator(RawTextComparator.DEFAULT);
        df.setDetectRenames(true);
        List<DiffEntry> diffs;
        diffs = df.scan(oldCommit.getTree(), newCommit.getTree());
        filesChanged = diffs.size();
        for (DiffEntry diff : diffs) {
            for (Edit edit : df.toFileHeader(diff).toEditList()) {
                linesDeleted += edit.getEndA() - edit.getBeginA();
                linesAdded += edit.getEndB() - edit.getBeginB();
            }
        }

        //        System.out.println("lines added: " + linesAdded + ", lines deleted: " + linesDeleted + " ,files changed: " + filesChanged);

        return new DiffResult(linesAdded, linesDeleted, filesChanged);
    }

    public static void main(String[] args) throws IOException, GitAPIException {
        // D:/IdeaProjects/mmd-restful-backend/src/main/resources
        String res = establishCommitHistory("D:/IdeaProjects/mmd-restful-backend/src/main/resources");
        System.out.println(res);
    }

//    public static void main(String[] args) throws IOException, GitAPIException {
//        // PART 0. preparation
//        Repository repository = findRepo("D:/IdeaProjects/mmd-restful-backend/src/main/resources");
//        Git git = new Git(repository);
//
//        // PART 1. List commits on all branches
//        // use the following instead to list commits on a specific branch
//        //ObjectId branchId = repository.resolve("HEAD");
//        //Iterable<RevCommit> commits = git.log().add(branchId).call();
//
//        List<SimpleCommit> simpleCommitList = new ArrayList<>();
//
//        Iterable<RevCommit> commits = git.log().all().call();
//        int count = 0;
//        for (RevCommit commit : commits) {
//            System.out.println("");
//            System.out.println("LogCommit: " + commit);
//            System.out.println("Parent count:" + commit.getParentCount());
//
//            LocalDateTime commitTime = Instant.ofEpochSecond(commit.getCommitTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
//            System.out.println("Commit Time:" + commitTime);
//
//            if (commit.getParentCount() == 1) {
//                DiffResult diffResult = diffCommits(git, commit, commit.getParent(0));
//                SimpleCommit sc = new SimpleCommit(commit.getId().toString().substring(0, 8), commit.getCommitTime(), diffResult.linesAdded, diffResult.lineDeleted);
//                simpleCommitList.add(sc);
//            }
//
//            count++;
//        }
//        System.out.println(count);
//
//        String commitHistory = new Gson().toJson(simpleCommitList);
//        System.out.println(commitHistory);
//    }
}
