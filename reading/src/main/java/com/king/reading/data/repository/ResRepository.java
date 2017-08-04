package com.king.reading.data.repository;

import com.blankj.utilcode.util.EmptyUtils;
import com.google.common.base.Optional;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.king.reading.common.utils.Check;
import com.king.reading.data.db.AppDatabase;
import com.king.reading.data.entities.BookBaseEntity;
import com.king.reading.data.entities.BookBaseEntity_Table;
import com.king.reading.data.entities.BookEntity;
import com.king.reading.data.entities.BookEntity_Table;
import com.king.reading.data.entities.DbMappers;
import com.king.reading.data.entities.ModuleEntity;
import com.king.reading.data.entities.ModuleEntity_Table;
import com.king.reading.data.entities.PageEntity;
import com.king.reading.data.entities.PageEntity_Table;
import com.king.reading.data.entities.PlayBooksEntity;
import com.king.reading.data.entities.PlayBooksEntity_Table;
import com.king.reading.data.entities.UnitEntity;
import com.king.reading.data.entities.UnitEntity_Table;
import com.king.reading.data.entities.WordEntity;
import com.king.reading.data.entities.WordEntity_Table;
import com.king.reading.ddb.GetBookResponse;
import com.king.reading.ddb.GetBooklistResponse;
import com.king.reading.ddb.GetPageResponse;
import com.king.reading.ddb.GetPlayBookResponse;
import com.king.reading.ddb.GetSecKeyResponse;
import com.king.reading.ddb.GetUnitWordsResponse;
import com.king.reading.ddb.GetWordsUnitListResponse;
import com.king.reading.ddb.SecKeyPair;
import com.king.reading.ddb.Unit;
import com.king.reading.model.WordSpellModule;
import com.king.reading.net.Api;
import com.king.reading.net.request.GetBookListReq;
import com.king.reading.net.request.GetBookReq;
import com.king.reading.net.request.GetPageReq;
import com.king.reading.net.request.GetPlayBookReq;
import com.king.reading.net.request.GetSecKeyReq;
import com.king.reading.net.request.GetUnitWordsReq;
import com.king.reading.net.request.GetWordsUnitListReq;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.rx2.language.RXSQLite;
import com.raizlabs.android.dbflow.rx2.structure.RXModelAdapter;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ProcessModelTransaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.MaybeEmitter;
import io.reactivex.MaybeOnSubscribe;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;


/**
 * Created by AllynYonge on 03/07/2017.
 */

@Singleton
public class ResRepository {
    private final Api api;
    private final Api testApi;
    private final UserRepository userRepository;
    private Map<Integer, SecKeyPair> secKeyPairs = Maps.newHashMap();

    @Inject
    public ResRepository(UserRepository userRepository, @Named("tars") Api api, @Named("gson") Api testApi) {
        this.api = api;
        this.testApi = testApi;
        this.userRepository = userRepository;
    }


    /**********************获取书本列表数据*************************/
    private Maybe<List<BookBaseEntity>> getBooksFromNet() {
        GetBookListReq req = new GetBookListReq(api);
        return req.sendRequest().map(new Function<GetBooklistResponse, List<BookBaseEntity>>() {
            @Override
            public List<BookBaseEntity> apply(@NonNull GetBooklistResponse response) throws Exception {
                return DbMappers.mapperBookList(response);
            }
        }).doOnSuccess(new Consumer<List<BookBaseEntity>>() {
            @Override
            public void accept(@NonNull List<BookBaseEntity> books) throws Exception {
                FlowManager.getModelAdapter(BookBaseEntity.class).saveAll(books);
            }
        }).toMaybe();
    }

    private Optional<BookBaseEntity> getBookBaseForId(final long bookId) {
        return Maybe.create(new MaybeOnSubscribe<Optional<BookBaseEntity>>() {
            @Override
            public void subscribe(@NonNull MaybeEmitter<Optional<BookBaseEntity>> e) throws Exception {
                e.onSuccess(Optional.fromNullable(SQLite.select()
                        .from(BookBaseEntity.class)
                        .where(BookBaseEntity_Table.bookId.eq(bookId))
                        .querySingle()));
            }
        }).filter(new Predicate<Optional<BookBaseEntity>>() {
            @Override
            public boolean test(@NonNull Optional<BookBaseEntity> bookBaseEntityOptional) throws Exception {
                return bookBaseEntityOptional.isPresent();
            }
        }).switchIfEmpty(getBooksFromNet().map(new Function<List<BookBaseEntity>, Optional<BookBaseEntity>>() {
            @Override
            public Optional<BookBaseEntity> apply(@NonNull List<BookBaseEntity> bookBaseEntities) throws Exception {
                for (BookBaseEntity bookBaseEntity : bookBaseEntities) {
                    if (bookBaseEntity.bookId == bookId)
                        return Optional.of(bookBaseEntity);
                }
                return Optional.absent();
            }
        })).blockingGet();
    }

    private List<BookBaseEntity> queryBooksForVersionFromDb(String version) {
        return SQLite.select()
                .from(BookBaseEntity.class)
                .where(BookBaseEntity_Table.areaName.eq(version))
                .queryList();
    }

    private List<BookBaseEntity> queryAllVersionsFromDb() {
        return SQLite.select().from(BookBaseEntity.class)
                .groupBy(BookBaseEntity_Table.areaName)
                .queryList();
    }

    /**
     * 根据地区名获取书本列表
     */
    public Maybe<List<BookBaseEntity>> getBooksForVersion(final String versionName) {
        return Maybe.concat(Maybe.create(new MaybeOnSubscribe<List<BookBaseEntity>>() {
            @Override
            public void subscribe(@NonNull MaybeEmitter<List<BookBaseEntity>> e) throws Exception {
                boolean isValid = false;
                List<BookBaseEntity> bookList = null;
                if (isValid) {
                    bookList = queryBooksForVersionFromDb(versionName);
                }
                bookList = Check.isEmpty(bookList) ? Lists.<BookBaseEntity>newArrayList() : bookList;
                e.onSuccess(bookList);
            }
        }).filter(new Predicate<List<BookBaseEntity>>() {
            @Override
            public boolean test(@NonNull List<BookBaseEntity> books) throws Exception {
                return Check.isNotEmpty(books);
            }
        }), getBooksFromNet().map(new Function<List<BookBaseEntity>, List<BookBaseEntity>>() {
            @Override
            public List<BookBaseEntity> apply(@NonNull List<BookBaseEntity> books) throws Exception {
                return queryBooksForVersionFromDb(versionName);
            }
        }).observeOn(AndroidSchedulers.mainThread()))
                .firstElement();
    }

    /**
     * 获取所有的地区列表
     */
    public Maybe<List<BookBaseEntity>> getAllVersions() {
        return Maybe.concat(Maybe.create(new MaybeOnSubscribe<List<BookBaseEntity>>() {
            @Override
            public void subscribe(@NonNull MaybeEmitter<List<BookBaseEntity>> e) throws Exception {
                boolean isValid = false;
                List<BookBaseEntity> bookBaseList = null;
                if (isValid) {
                    bookBaseList = queryAllVersionsFromDb();
                }
                bookBaseList = EmptyUtils.isEmpty(bookBaseList) ? new ArrayList<BookBaseEntity>() : bookBaseList;
                e.onSuccess(bookBaseList);
            }
        }).filter(new Predicate<List<BookBaseEntity>>() {
            @Override
            public boolean test(@NonNull List<BookBaseEntity> books) throws Exception {
                return EmptyUtils.isNotEmpty(books);
            }
        }), getBooksFromNet().map(new Function<List<BookBaseEntity>, List<BookBaseEntity>>() {
            @Override
            public List<BookBaseEntity> apply(@NonNull List<BookBaseEntity> books) throws Exception {
                return queryAllVersionsFromDb();
            }
        }).observeOn(AndroidSchedulers.mainThread()))
                .firstElement();
    }

    public Maybe<List<BookBaseEntity>> getBookBaseForId() {
        return Maybe.concat(Maybe.create(new MaybeOnSubscribe<List<BookBaseEntity>>() {
            @Override
            public void subscribe(@NonNull MaybeEmitter<List<BookBaseEntity>> e) throws Exception {
                boolean isValid = false;
                List<BookBaseEntity> bookBaseList = null;
                if (isValid) {
                    bookBaseList = queryAllVersionsFromDb();
                }
                bookBaseList = EmptyUtils.isEmpty(bookBaseList) ? new ArrayList<BookBaseEntity>() : bookBaseList;
                e.onSuccess(bookBaseList);
            }
        }).filter(new Predicate<List<BookBaseEntity>>() {
            @Override
            public boolean test(@NonNull List<BookBaseEntity> books) throws Exception {
                return EmptyUtils.isNotEmpty(books);
            }
        }), getBooksFromNet().map(new Function<List<BookBaseEntity>, List<BookBaseEntity>>() {
            @Override
            public List<BookBaseEntity> apply(@NonNull List<BookBaseEntity> books) throws Exception {
                return queryAllVersionsFromDb();
            }
        }).observeOn(AndroidSchedulers.mainThread()))
                .firstElement();
    }


    /**********************获取书本详细信息*************************/
    private Maybe<BookEntity> getBookDetailFromNet(long bookId) {
        GetBookReq req = new GetBookReq(api, bookId);
        return req.sendRequest().map(new Function<GetBookResponse, BookEntity>() {
            @Override
            public BookEntity apply(@NonNull GetBookResponse response) throws Exception {
                BookBaseEntity bookBaseEntity = getBookBaseForId(response.book.base.bookID).get();
                bookBaseEntity.resourceId = response.book.getSecKeyPair().resourceID;
                bookBaseEntity.save();
                return DbMappers.mapperBook(response);
            }
        }).doOnSuccess(new Consumer<BookEntity>() {
            @Override
            public void accept(@NonNull BookEntity book) throws Exception {
                ProcessModelTransaction.Builder transaction = new ProcessModelTransaction.Builder(new ProcessModelTransaction.ProcessModel<BookEntity>() {
                    @Override
                    public void processModel(BookEntity entity, DatabaseWrapper wrapper) {
                        entity.save();
                        for (ModuleEntity moduleEntity : entity.modules) {
                            FlowManager.getModelAdapter(UnitEntity.class).saveAll(moduleEntity.units);
                        }
                    }
                }).add(book);
                FlowManager.getDatabase(AppDatabase.class).executeTransaction(transaction.build());
            }
        }).toMaybe();
    }

    private BookEntity queryBookDetailFromDb(long id) {
        return SQLite.select()
                .from(BookEntity.class)
                .where(BookEntity_Table.bookId.eq(id))
                .querySingle();
    }

    public Maybe<BookEntity> getBookDetail(final long bookId) {
        return Maybe.concat(Maybe.create(new MaybeOnSubscribe<BookEntity>() {
            @Override
            public void subscribe(@NonNull MaybeEmitter<BookEntity> e) throws Exception {
                boolean isValid = false;
                BookEntity book = null;
                if (isValid) {
                    book = queryBookDetailFromDb(bookId);
                }
                book = book == null ? new BookEntity() : book;
                e.onSuccess(book);
            }
        }).filter(new Predicate<BookEntity>() {
            @Override
            public boolean test(@NonNull BookEntity book) throws Exception {
                return book.bookId != 0;
            }
        }), getBookDetailFromNet(bookId).map(new Function<BookEntity, BookEntity>() {
            @Override
            public BookEntity apply(@NonNull BookEntity book) throws Exception {
                return queryBookDetailFromDb(bookId);
            }
        }).observeOn(AndroidSchedulers.mainThread()))
                .firstElement();
    }

    /**********************获取Page页信息*************************/
    public Maybe<List<PageEntity>> getPagesFromNet(final int useResourceId, int start, int end) {
        GetPageReq pageReq = new GetPageReq(api, useResourceId, start, end);
        return pageReq.sendRequest().map(new Function<GetPageResponse, List<PageEntity>>() {
            @Override
            public List<PageEntity> apply(@NonNull GetPageResponse getPageResponse) throws Exception {
                List<PageEntity> pages = DbMappers.mapperPage(getPageResponse, useResourceId);
                return pages;
            }
        }).doOnSuccess(new Consumer<List<PageEntity>>() {
            @Override
            public void accept(@NonNull List<PageEntity> pageEntities) throws Exception {
                RXModelAdapter.from(PageEntity.class).saveAll(pageEntities).subscribe();
            }
        }).toMaybe();
    }

    public List<PageEntity> queryPagesFromDb(long resourceId, int start, int end) {
        return SQLite.select().from(PageEntity.class)
                .where(PageEntity_Table.number.between(start).and(end),
                        PageEntity_Table.id.between(Integer.parseInt("" + resourceId + start)).and(Integer.parseInt("" + resourceId + end)))
                .orderBy(PageEntity_Table.number, true)
                .queryList();
    }

    public Maybe<List<PageEntity>> getRangePages(final int resourceId, final int start, final int end) {
        return Maybe.concat(Maybe.create(new MaybeOnSubscribe<List<PageEntity>>() {
            @Override
            public void subscribe(@NonNull MaybeEmitter<List<PageEntity>> e) throws Exception {
                boolean isValid = true;
                List<PageEntity> pages = null;
                if (isValid) {
                    pages = queryPagesFromDb(resourceId, start, end);
                }
                pages = Check.isEmpty(pages) ? new ArrayList<PageEntity>() : pages;
                e.onSuccess(pages);
            }
        }).filter(new Predicate<List<PageEntity>>() {
            @Override
            public boolean test(@NonNull List<PageEntity> pages) throws Exception {
                return Check.isNotEmpty(pages);
            }
        }), getPagesFromNet(resourceId, start, end))
        .firstElement().observeOn(AndroidSchedulers.mainThread());
    }

    /**********************获取对应资源ID的密钥*************************/
    public Maybe<SecKeyPair> getSecKey() {
        final int resourceId = userRepository.getUseResourceId();
        GetSecKeyReq req = new GetSecKeyReq(api, resourceId);
        return Maybe.create(new MaybeOnSubscribe<SecKeyPair>() {
            @Override
            public void subscribe(@NonNull MaybeEmitter<SecKeyPair> e) throws Exception {
                if (secKeyPairs.get(resourceId) != null) {
                    e.onSuccess(secKeyPairs.get(resourceId));
                    e.onComplete();
                } else
                    e.onComplete();
            }
        }).switchIfEmpty(req.sendRequest().map(new Function<GetSecKeyResponse, SecKeyPair>() {
            @Override
            public SecKeyPair apply(@NonNull GetSecKeyResponse getSecKeyResponse) throws Exception {
                return getSecKeyResponse.getPair();
            }
        }).doOnSuccess(new Consumer<SecKeyPair>() {
            @Override
            public void accept(@NonNull SecKeyPair secKeyPair) throws Exception {
                secKeyPairs.put(resourceId, secKeyPair);
            }
        }).toMaybe());
    }

    /**********************获取单词拼写目录结构*************************/
    private Single<List<WordSpellModule>> getWordListForNet(final long bookId, final int resourceId){
        GetWordsUnitListReq req = new GetWordsUnitListReq(api, bookId, resourceId);
        return req.sendRequest().map(new Function<GetWordsUnitListResponse, List<WordSpellModule>>() {
            @Override
            public List<WordSpellModule> apply(@NonNull GetWordsUnitListResponse getWordsUnitListResponse) throws Exception {
                return DbMappers.mapperUnitWords(getWordsUnitListResponse.getUnits());
            }
        }).doOnSuccess(new Consumer<List<WordSpellModule>>() {
            @Override
            public void accept(@NonNull List<WordSpellModule> unitEntities) throws Exception {
                List<Long> list = Lists.newArrayList();
                for (WordSpellModule unit : unitEntities) {
                    list.add(unit.unitId);
                }
                SQLite.update(UnitEntity.class).set(UnitEntity_Table.isWord.is(true))
                        .where(UnitEntity_Table.resourceId.eq(resourceId))
                        .and(UnitEntity_Table.unitId.in(list))
                        .execute();

                SQLite.update(ModuleEntity.class).set(ModuleEntity_Table.isWord.is(true))
                        .where(ModuleEntity_Table.resourceId.eq(resourceId))
                        .and(ModuleEntity_Table.moduleId.in(list))
                        .execute();
            }
        }).observeOn(AndroidSchedulers.mainThread());
    }

    private Single<List<WordSpellModule>> getWordUnitForDb(int resourceId){
        return RXSQLite.rx(SQLite.select().from(ModuleEntity.class)
                .where(ModuleEntity_Table.resourceId.eq(resourceId))
                .and(ModuleEntity_Table.isWord.eq(true)))
                .queryList().map(new Function<List<ModuleEntity>, List<WordSpellModule>>() {
                    @Override
                    public List<WordSpellModule> apply(@NonNull List<ModuleEntity> unitEntities) throws Exception {
                        List<WordSpellModule> list = Lists.newArrayList();
                        for (ModuleEntity moduleEntity : unitEntities) {
                            list.add(new WordSpellModule(moduleEntity.title, moduleEntity.moduleId));
                        }
                        return list;
                    }
                }).zipWith(RXSQLite.rx(SQLite.select().from(UnitEntity.class)
                        .where(UnitEntity_Table.resourceId.eq(resourceId))
                        .and(UnitEntity_Table.isWord.eq(true)))
                        .queryList().map(new Function<List<UnitEntity>, List<WordSpellModule>>() {
                            @Override
                            public List<WordSpellModule> apply(@NonNull List<UnitEntity> unitEntities) throws Exception {
                                List<WordSpellModule> list = Lists.newArrayList();
                                for (UnitEntity unitEntity : unitEntities) {
                                    list.add(new WordSpellModule(unitEntity.name, unitEntity.unitId));
                                }
                                return list;
                            }
                        }), new BiFunction<List<WordSpellModule>, List<WordSpellModule>, List<WordSpellModule>>() {
                    @Override
                    public List<WordSpellModule> apply(@NonNull List<WordSpellModule> unitEntities, @NonNull List<WordSpellModule> unitEntities2) throws Exception {
                        unitEntities.addAll(unitEntities2);
                        Collections2.orderedPermutations(unitEntities);
                        return unitEntities;
                    }
                });
    }

    public Flowable<List<WordSpellModule>> getWordUnit(){
        int resourceId = userRepository.getUseResourceId();
        long bookId = userRepository.getBookId();
        return getWordUnitForDb(resourceId).mergeWith(getWordListForNet(bookId, resourceId));
    }

    /**********************获取单词列表*************************/
    private Maybe<List<WordEntity>> getWordsFromNet(final int resourceId, final long unitId) {
        GetUnitWordsReq req = new GetUnitWordsReq(api, resourceId, unitId);
        return req.sendRequest().map(new Function<GetUnitWordsResponse, List<WordEntity>>() {
            @Override
            public List<WordEntity> apply(@NonNull GetUnitWordsResponse getUnitWordsResponse) throws Exception {
                return DbMappers.mapperWords(resourceId, unitId, getUnitWordsResponse.getWords());
            }
        }).doOnSuccess(new Consumer<List<WordEntity>>() {
            @Override
            public void accept(@NonNull List<WordEntity> wordEntities) throws Exception {
                FlowManager.getModelAdapter(WordEntity.class).saveAll(wordEntities);
            }
        }).toMaybe();
    }

    private List<WordEntity> queryWordsFromDb(int resourceId, long unitId) {
        return SQLite.select().from(WordEntity.class)
                .where(WordEntity_Table.id.like(String.valueOf(resourceId + unitId)))
                .queryList();
    }

    public Maybe<List<WordEntity>> getWords(final long unitId) {
        final int resourceId = userRepository.getUseResourceId();
        return Maybe.concat(Maybe.create(new MaybeOnSubscribe<List<WordEntity>>() {
            @Override
            public void subscribe(@NonNull MaybeEmitter<List<WordEntity>> e) throws Exception {
                boolean isValid = true;
                List<WordEntity> words = null;
                if (isValid) {
                    words = queryWordsFromDb(resourceId, unitId);
                }
                words = Check.isEmpty(words) ? new ArrayList<WordEntity>() : words;
                e.onSuccess(words);
            }
        }).filter(new Predicate<List<WordEntity>>() {
            @Override
            public boolean test(@NonNull List<WordEntity> wordEntities) throws Exception {
                return Check.isNotEmpty(wordEntities);
            }
        }), getWordsFromNet(resourceId, unitId))
        .firstElement()
        .observeOn(AndroidSchedulers.mainThread());
    }

    /**********************获取角色扮演*************************/
    private Maybe<PlayBooksEntity> getPlayBooksFromNet(final int resourceId, final long unitId) {
        GetPlayBookReq req = new GetPlayBookReq(api, resourceId, unitId);
        return req.sendRequest().map(new Function<GetPlayBookResponse, PlayBooksEntity>() {
            @Override
            public PlayBooksEntity apply(@NonNull GetPlayBookResponse response) throws Exception {
                return DbMappers.mapperPlayBooks(resourceId, unitId, response);
            }
        }).doOnSuccess(new Consumer<PlayBooksEntity>() {
            @Override
            public void accept(@NonNull PlayBooksEntity entity) throws Exception {
                entity.save();
            }
        }).toMaybe();
    }

    private PlayBooksEntity getPlayBooksFromDb(final int resourceId, final long unitId) {
        return SQLite.select().from(PlayBooksEntity.class)
                .where(PlayBooksEntity_Table.id.eq("" + resourceId + unitId))
                .querySingle();
    }

    public Maybe<PlayBooksEntity> getPlayBooks(final int resourceId, final long unitId) {
        return Maybe.concat(Maybe.create(new MaybeOnSubscribe<PlayBooksEntity>() {
            @Override
            public void subscribe(@NonNull MaybeEmitter<PlayBooksEntity> e) throws Exception {
                boolean isValid = false;
                PlayBooksEntity entity = null;
                if (isValid) {
                    entity = getPlayBooksFromDb(resourceId, unitId);
                }
                entity = entity == null ? new PlayBooksEntity() : entity;
                e.onSuccess(entity);
            }
        }).filter(new Predicate<PlayBooksEntity>() {
            @Override
            public boolean test(@NonNull PlayBooksEntity entity) throws Exception {
                return Check.isNotEmpty(entity.id);
            }
        }), getPlayBooksFromNet(resourceId, unitId))
                .firstElement();
    }

    public Maybe<PlayBooksEntity> getPlayBooks(long unitId) {
        return getPlayBooks(userRepository.getUseResourceId(), unitId);
    }

}
