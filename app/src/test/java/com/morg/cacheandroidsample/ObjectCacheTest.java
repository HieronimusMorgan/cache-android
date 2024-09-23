package com.morg.cacheandroidsample;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ObjectCacheTest {

    @Mock
    Context mockContext;

    private ObjectCache<List<Student>> cache;
    private File mockFile;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        cache = new ObjectCache<>(mockContext);
        mockFile = mock(File.class);
    }

    @Test
    public void getValue_returnsCorrectValue_whenFileExists() throws IOException {
        // Arrange
        String key = "students";
        Type listType = new TypeToken<List<Student>>() {
        }.getType();
        List<Student> expectedStudents = new ArrayList<>();
        expectedStudents.add(new Student("Test 1", "Class 1"));
        expectedStudents.add(new Student("Test 2", "Class 2"));

        File cacheDir = new File(mockContext.getCacheDir().getAbsolutePath());
        File cacheFile = new File(cacheDir, key + ".txt");

        when(mockContext.getCacheDir()).thenReturn(cacheDir);
        when(mockFile.exists()).thenReturn(true);
        when(mockFile.getPath()).thenReturn(cacheFile.getPath());

        try (FileWriter writer = new FileWriter(cacheFile)) {
            writer.write(new Gson().toJson(expectedStudents));
        }

        // Act
        List<Student> actualStudents = cache.getValue(key, listType);

        // Assert
        assertEquals(expectedStudents, actualStudents);
    }

    @Test
    public void getValue_returnsNull_whenFileDoesNotExist() {
        // Arrange
        String key = "students";
        Type listType = new TypeToken<List<Student>>() {
        }.getType();

        when(mockContext.getCacheDir()).thenReturn(new File(mockContext.getCacheDir(), key + ".txt"));
        when(mockFile.exists()).thenReturn(false);

        // Act
        List<Student> actualStudents = cache.getValue(key, listType);

        // Assert
        assertNull(actualStudents);
    }

    @Test
    public void setValue_savesValueCorrectly() throws IOException {
        // Arrange
        String key = "students";
        List<Student> students = new ArrayList<>();
        students.add(new Student("Test 1", "Class 1"));
        students.add(new Student("Test 2", "Class 2"));

        when(mockContext.getCacheDir()).thenReturn(new File("cache"));

        // Act
        cache.setValue(key, students);

        // Assert
        File file = new File(mockContext.getCacheDir(), key + ".txt");
        assertTrue(file.exists());
    }

    @Test
    public void deleteValue_deletesFile_whenFileExists() {
        // Arrange
        String key = "students";

        when(mockContext.getCacheDir()).thenReturn(new File(mockContext.getCacheDir(), key + ".txt"));
        when(mockFile.exists()).thenReturn(true);
        when(mockFile.delete()).thenReturn(true);

        // Act
        cache.deleteValue(key);

        // Assert
        verify(mockFile).delete();
    }

    @Test
    public void deleteValue_doesNothing_whenFileDoesNotExist() {
        // Arrange
        String key = "students";

        when(mockContext.getCacheDir()).thenReturn(new File(mockContext.getCacheDir().getAbsolutePath()));
        when(mockFile.exists()).thenReturn(false);

        // Act
        cache.deleteValue(key);

        // Assert
        verify(mockFile, never()).delete();
    }
}